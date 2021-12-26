<?php
function get_encoder($table): string
{
    if ($table == "culture") {
        return "gbk";
    } else {
        return "utf8";
    }
}

header("Content-type: text/html; charset=utf-8");

$username = "root";
$password = "2333";

if (!isset($_GET['table']) or !isset($_GET['utime'])){
    echo "Bad params.";
    return;
}

$table_name = $_GET['table'];
$update_time = $_GET['utime'];
$encoder = get_encoder($table_name);

$linked = new mysqli("localhost:3306", $username, $password);
if ($linked->connect_error) {
    die("Connection failed: " . $linked->connect_error);
}
$linked->query("SET NAMES '{$encoder}'");
$linked->select_db("news");

$query = "SELECT * FROM {$table_name} WHERE utime='{$update_time}'";
$res = $linked->query($query);

$data = array();
while ($row = $res->fetch_assoc()) {
    $data[] = $row;
}

if ($encoder == "gbk") {
    $data = mb_convert_encoding($data, "UTF-8", "GBK");
}

echo json_encode($data, JSON_UNESCAPED_UNICODE);

