/*
 * STM32L051K8 Driver
 *
 * Copyright (c) 2018 Alan Wang
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or (at your option) any later version.
 */

#include <linux/types.h>
#include <linux/module.h>
#include <linux/init.h>
#include <linux/delay.h>
#include <linux/slab.h>
#include <linux/interrupt.h>
#include <linux/workqueue.h>
#include <linux/gpio.h>
#include <linux/i2c.h>
#include <linux/input.h>
#include <linux/of.h>
#include <linux/kobject.h>
#include <linux/device.h>

#define MCU_Status     0x01
#define SN_Write       0x02
#define SN_Read        0x03
#define PN_Write       0x04
#define PN_Read        0x05
#define MCU_version    0x0F
#define SET_POWER_ON_DELAY_TIME	0x10
#define GET_POWER_ON_DELAY_TIME	0x11
#define SET_POWER_OFF_DELAY_TIME	0x12
#define GET_POWER_OFF_DELAY_TIME	0x13
#define Bat_Vol        0x20
#define Bat_get_alarm  0x21
#define Bat_set_alarm  0x22
#define POWER_OFF_IGNITION_Happen 0x80
#define Bat_Happen     0x02
#define Bat_addr       0x11
#define Mcu_addr       0x25
#define buf_size       17


static struct i2c_driver blta_stm32l051k8_driver;

struct blta_stm32l051k8_data {
	struct	i2c_client *client;
	int		irq;
};

static int mcu_write(struct i2c_client *client, u8 reg, const u8 *buf, int len)
{
	struct i2c_msg xfer[1];
	u8 data_buf[buf_size+1];
	int error,i=0;

	memset(data_buf, 0, sizeof(data_buf));
	memset(xfer, 0, sizeof(xfer));

	data_buf[0] = reg;

	for(i=1; i<=buf_size ;i++)
	{
		data_buf[i] = buf[i-1];
	}

	xfer[0].addr = client->addr;
	xfer[0].flags = 0;
	xfer[0].len = len+1;
	xfer[0].buf = data_buf;

	error = i2c_transfer(client->adapter, xfer, 1);
	if (error != 1) {
		dev_err(&client->dev, "%s: i2c transfer failed (%d)\n", __func__, error);
		return error < 0 ? error : -EIO;
	}
	return 0;
}

static int mcu_read(struct i2c_client *client, u8 reg, u8 *buf, int len)
{
	struct i2c_msg xfer[2];
	int error;

	memset(xfer, 0, sizeof(xfer));

	xfer[0].addr = client->addr;
	xfer[0].flags = 0;
	xfer[0].len = 1;
	xfer[0].buf = &reg;

	xfer[1].addr = client->addr;
	xfer[1].flags = I2C_M_RD;
	xfer[1].len = len;
	xfer[1].buf = buf;

	error = i2c_transfer(client->adapter, xfer, 2);
	if (error != 2) {
		dev_err(&client->dev, "%s: i2c transfer failed (%d)\n", __func__, error);
		return error < 0 ? error : -EIO;
	}

	return 0;
}

static ssize_t mcu_status_show(struct device *dev, struct device_attribute *attr, char *buf)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;
	u8 data_buf[buf_size];

	memset(data_buf, 0, sizeof(data_buf));

	error = mcu_read(mcu_data->client, MCU_Status, data_buf, 1);
	if (error)
	{
		printk("mcu_status_show I2C transfer error: %d\n", error);
		return error;
	}

	return sprintf(buf, "%x\n", data_buf[0]);
}

static ssize_t power_on_delay_time_show(struct device *dev, struct device_attribute *attr, char *buf)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;
	u8 data_buf[buf_size];
	u16 bat_vol_data;

	memset(data_buf, 0, sizeof(data_buf));
	mcu_data->client->addr = Bat_addr;
	error = mcu_read(mcu_data->client, GET_POWER_ON_DELAY_TIME, data_buf, 2);
	mcu_data->client->addr = Mcu_addr;
	if (error)
	{
		printk("power_on_delay_time_show I2C transfer error: %d\n", error);
		return error;
	}

	bat_vol_data = (data_buf[0] << 8) | data_buf[1];
	return sprintf(buf, "%02d\n", bat_vol_data);
}


static ssize_t power_on_delay_time_store(struct device *dev, struct device_attribute *attr, const char *buf, size_t size)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;
	u8 data_buf[buf_size];
	int val;

	memset(data_buf, 0, sizeof(data_buf));
	sscanf(buf, "%d", &val);
	data_buf[0] = val >> 8;
	data_buf[1] = val & 0x00ff;

	mcu_data->client->addr = Bat_addr;
	error = mcu_write(mcu_data->client, SET_POWER_ON_DELAY_TIME, data_buf, 2);
	mcu_data->client->addr = Mcu_addr;
	if (error)
	{
		printk("power_on_delay_time_store I2C transfer error: %d\n", error);
		return error;
	}

	return size;
}

static ssize_t power_off_delay_time_show(struct device *dev, struct device_attribute *attr, char *buf)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;
	u8 data_buf[buf_size];
	u16 bat_vol_data;

	memset(data_buf, 0, sizeof(data_buf));
	mcu_data->client->addr = Bat_addr;
	error = mcu_read(mcu_data->client, GET_POWER_OFF_DELAY_TIME, data_buf, 2);
	mcu_data->client->addr = Mcu_addr;
	if (error)
	{
		printk("power_off_delay_time_show I2C transfer error: %d\n", error);
		return error;
	}

	bat_vol_data = (data_buf[0] << 8) | data_buf[1];

	return sprintf(buf, "%02d\n", bat_vol_data);
}


static ssize_t power_off_delay_time_store(struct device *dev, struct device_attribute *attr, const char *buf, size_t size)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;
	u8 data_buf[buf_size];
	int val;

	memset(data_buf, 0, sizeof(data_buf));
	sscanf(buf, "%d", &val);
	data_buf[0] = val >> 8;
	data_buf[1] = val & 0x00ff;

	mcu_data->client->addr = Bat_addr;
	error = mcu_write(mcu_data->client, SET_POWER_OFF_DELAY_TIME, data_buf, 2);
	mcu_data->client->addr = Mcu_addr;
	if (error)
	{
		printk("power_off_delay_time_store I2C transfer error: %d\n", error);
		return error;
	}

	return size;
}

static ssize_t sn_rw_show(struct device *dev, struct device_attribute *attr, char *buf)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;
	u8 data_buf[buf_size];

	memset(data_buf, 0, sizeof(data_buf));

	error = mcu_read(mcu_data->client, SN_Read, data_buf, 12);
	if (error)
	{
		printk("sn_read_show I2C transfer error: %d\n", error);
		return error;
	}

	return sprintf(buf, "%s\n", data_buf);
}

static ssize_t sn_rw_store(struct device *dev, struct device_attribute *attr, const char *buf, size_t size)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;

	error = mcu_write(mcu_data->client, SN_Write, buf, 12);
	if (error)
	{
		printk("sn_rw_store I2C transfer error: %d\n", error);
		return error;
	}

	return size;
}

static ssize_t pn_rw_show(struct device *dev, struct device_attribute *attr, char *buf)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;

	u8 data_buf[buf_size];

	memset(data_buf, 0, sizeof(data_buf));

	error = mcu_read(mcu_data->client, PN_Read, data_buf, 16);
	if (error)
	{
		printk("pn_read_show I2C transfer error: %d\n", error);
		return error;
	}

	data_buf[15]='\0';

	return sprintf(buf, "%s\n", data_buf);
}

static ssize_t pn_rw_store(struct device *dev, struct device_attribute *attr, const char *buf, size_t size)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;

	error = mcu_write(mcu_data->client, PN_Write, buf, 16);
	if (error)
	{
		printk("pn_rw_store I2C transfer error: %d\n", error);
		return error;
	}

	return size;
}

static ssize_t mcu_version_show(struct device *dev, struct device_attribute *attr, char *buf)
{
	struct i2c_client *client = to_i2c_client(dev);
	struct blta_stm32l051k8_data *mcu_data = i2c_get_clientdata(client);
	int error;
	u8 data_buf[buf_size];
	u16 bat_vol_data;

	memset(data_buf, 0, sizeof(data_buf));

	error = mcu_read(mcu_data->client, MCU_version, data_buf, 2);
	if (error)
	{
		printk("mcu_version_show I2C transfer error: %d\n", error);
		return error;
	}

	bat_vol_data = (data_buf[0] << 8) | data_buf[1];
	return sprintf(buf, "%04d\n", bat_vol_data);
}

static DEVICE_ATTR(mcu_status, S_IRUGO, mcu_status_show, NULL);
static DEVICE_ATTR(powerondelaytime, S_IRUGO | S_IWUSR, power_on_delay_time_show, power_on_delay_time_store);
static DEVICE_ATTR(poweroffdelaytime, S_IRUGO | S_IWUSR, power_off_delay_time_show, power_off_delay_time_store);
static DEVICE_ATTR(sn_rw, S_IRUGO | S_IWUSR, sn_rw_show, sn_rw_store);
static DEVICE_ATTR(pn_rw, S_IRUGO | S_IWUSR, pn_rw_show, pn_rw_store);
static DEVICE_ATTR(mcu_version, S_IRUGO, mcu_version_show, NULL);
static struct attribute *mcu_attrs[] = {
	&dev_attr_mcu_status.attr,
	&dev_attr_powerondelaytime.attr,
	&dev_attr_poweroffdelaytime.attr,
	&dev_attr_sn_rw.attr,
	&dev_attr_pn_rw.attr,
	&dev_attr_mcu_version.attr,
	NULL,
};

static struct attribute_group mcu_attr_group = {
	.attrs = mcu_attrs,
};

static int dev_create_sysfs(struct device *dev)
{
	int rc;

	rc = sysfs_create_group(&dev->kobj, &mcu_attr_group);
	if (rc)
		printk("sysfs group creation failed, rc=%d\n", rc);
	return rc;
}

static irqreturn_t blta_stm32l051k8_interrupt(int irq, void *data)
{
	struct blta_stm32l051k8_data *mcu_data = data;
	int error;
	/*u8 val_ta;*/
	u8 data_buf[buf_size];
	char event_string[16];
	char *envp[] = { event_string, NULL };

	memset(data_buf, 0, sizeof(data_buf));

	error = mcu_read(mcu_data->client, MCU_Status, data_buf, 1);
	if (error)
	{
		printk("I2C transfer error: %d\n", error);
		return error;
	}

	if (data_buf[0] & POWER_OFF_IGNITION_Happen)
	{
		sprintf(event_string, "EVENT=PowerOffIgnition");
		kobject_uevent_env(&mcu_data->client->dev.kobj, KOBJ_CHANGE, envp);
	}

	return IRQ_HANDLED;
}

static int blta_stm32l051k8_probe(struct i2c_client *client, const struct i2c_device_id *id)
{
	struct blta_stm32l051k8_data *blta_data;
	int error;

	if (!i2c_check_functionality(client->adapter, I2C_FUNC_I2C))
	{
		dev_err(&client->dev, "I2C_FUNC_I2C not Supported\n");
		return -ENODEV;
	}

	if (!client->irq) {
		dev_err(&client->dev, "no IRQ?\n");
		return -EINVAL;
	}

	blta_data = devm_kzalloc(&client->dev, sizeof(*blta_data), GFP_KERNEL);

	if (!blta_data) {
		dev_err(&client->dev, "devm_kzalloc failed\n");
		return -ENOMEM;
	}

	blta_data->client = client;
	blta_data->irq = client->irq;

	error = devm_request_threaded_irq(&client->dev, client->irq, NULL, blta_stm32l051k8_interrupt,
				IRQF_TRIGGER_FALLING | IRQF_ONESHOT | IRQF_SHARED, client->name, blta_data);

	if (error)
	{
		dev_err(&client->dev, "Unable to claim irq %d; error %d\n", client->irq, error);
		return -ENOMEM;
	}

	i2c_set_clientdata(client, blta_data);
	dev_create_sysfs(&client->dev);

	return 0;
}

static const struct i2c_device_id blta_stm32l051k8_id[] = {
	{ "blta_stm32l051k8", 0 },
	{ }
};
MODULE_DEVICE_TABLE(i2c, blta_stm32l051k8_id);

#ifdef CONFIG_OF
static const struct of_device_id blta_stm32l051k8_ids[] = {
	{ .compatible = "blta_stm32l051k8", },
	{ }
};
MODULE_DEVICE_TABLE(of, blta_stm32l051k8_ids);
MODULE_ALIAS("i2c:blta_stm32l051k8");
#endif

static struct i2c_driver blta_stm32l051k8_driver = {
	.driver		= {
		.name	= "blta_stm32l051k8",
		.owner	= THIS_MODULE,
		.of_match_table = of_match_ptr(blta_stm32l051k8_ids),

	},
	.probe		= blta_stm32l051k8_probe,
	.id_table	= blta_stm32l051k8_id,
};

static int __init blta_stm32l051k8_init(void)
{
	return i2c_add_driver(&blta_stm32l051k8_driver);
}

static void __exit blta_stm32l051k8_exit(void)
{
	i2c_del_driver(&blta_stm32l051k8_driver);
}

module_init(blta_stm32l051k8_init);
module_exit(blta_stm32l051k8_exit);

MODULE_AUTHOR("Alan Wang <alan.wang@dfi.com>");
MODULE_DESCRIPTION("STM32L051K8 driver");
MODULE_LICENSE("GPL");
