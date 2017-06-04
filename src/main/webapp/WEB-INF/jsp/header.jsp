<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <link rel="icon" href="/img/train.jpg">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/css/railroad_main.css">
    <link rel="stylesheet" href="/css/bootstrap-datetimepicker_min.css">
    <script src="/js/jquery-1.11.1.min.js"></script>
    <script src="/js/moment-with-locales.min.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
    <title>${title}</title>
</head>
<body>
<div class="container">
    <div class="row">
        <span align="right">User: ${user.name}</span>
        <a href="/user/tickets">My tickets</a>
        <div class="wrapper">
            <header class="main_header">
                <div>
                    RailTrans
                </div>
            </header>
            <nav class="main_menu">
                <a href="/routes">
                    <div> Routes</div>
                </a>
                <a href="/stations">
                    <div> Stations</div>
                </a>
                <a href="/findway">
                    <div> Tickets</div>
                </a>
                <a href="/trains">
                    <div> Trains</div>
                </a>
                <a href="/out">
                    <div> Log out</div>
                </a>
                <a>
                    <div class="train_head">
                        <img class="train_head" src="/img/train_head.png">
                    </div>
                </a>
            </nav>