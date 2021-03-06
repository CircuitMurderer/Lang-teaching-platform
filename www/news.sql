DROP DATABASE IF EXISTS news;
CREATE DATABASE news;
USE news;

DROP TABLE IF EXISTS `culture`;
CREATE TABLE `culture` (
    title varchar(100) PRIMARY KEY NOT NULL,
    time varchar(50) NOT NULL,
    text text NOT NULL,
    utime varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

DROP TABLE IF EXISTS `domestic`;
CREATE TABLE `domestic` (
    title varchar(100) PRIMARY KEY NOT NULL,
    time varchar(50) NOT NULL,
    text text NOT NULL,
    utime varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `world`;
CREATE TABLE `world` (
    title varchar(100) PRIMARY KEY NOT NULL,
    time varchar(50) NOT NULL,
    text text NOT NULL,
    utime varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gushiwen`;
CREATE TABLE `gushiwen` (
    title varchar(100) PRIMARY KEY NOT NULL,
    time varchar(50) NOT NULL,
    text text NOT NULL,
    utime varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `expert`;
CREATE TABLE `expert` (
    title varchar(100) PRIMARY KEY NOT NULL,
    time varchar(50) NOT NULL,
    text text NOT NULL,
    utime varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `doublelang`;
CREATE TABLE `doublelang` (
    title varchar(100) PRIMARY KEY NOT NULL,
    time varchar(50) NOT NULL,
    text text NOT NULL,
    utime varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `intensive`;
CREATE TABLE `intensive` (
    title varchar(100) PRIMARY KEY NOT NULL,
    time varchar(50) NOT NULL,
    text text NOT NULL,
    utime varchar(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


