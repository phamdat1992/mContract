# mContract-web

## Setup certification key cho jre trên thiết bị 

### 1. mở command prompt với quyền admination
* với MacOS, Ubunbu, Linux, ... chạy lệnh sudo
### 2. chạy lệnh add certification key
$ keytool -importcert -file <path tới file .cer> -cacerts
default password : changeit
### 3. list all certification keys để kiểm tra đã add thành công chưa
keytool -importcert -file <path tới file .cer> -cacerts