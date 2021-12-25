<?php
$username = "root";
$password = "2333";

if (!isset($_GET['table']) or !isset($_GET['utime'])){
    echo "Bad params.<br>";
    return;
}

$linked = new mysqli("localhost:3306", $username, $password);
if ($linked->connect_error) {
    die("Connection failed: " . $linked->connect_error);
}

$linked->select_db("news");
$query = "SELECT * FROM {$_GET['table']} WHERE utime='{$_GET['utime']}'";
$res = $linked->query($query);

$data = $res->fetch_all();
echo json_encode($data);

