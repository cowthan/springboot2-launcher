

#!/bin/sh

echo "=============== 删除  代码 =============================="
rm -rf source-satgw-chatdemo/

echo "=============== clone代码 ==============================="

git clone -b develop http://geesatcom_paas_satgw:xiy6uxEs7fDbX8RTU3GD@gitlab.geesatcom.io/satellite-gateway-platform/satgw-core.git source-satgw-persist
cd source-satgw-persist
cd ../

echo "================ maven 编译代码 ========================================="

cd source-satgw-persist
mvn clean package -Dmaven.test.skip=true -U
echo "=================copy bootstrap.jar 到 /var/www/html/backend ============="
cp satgw-persist/satgw-persist-bootstrap/target/satgw-persist-bootstrap-1.0.5-SNAPSHOT.jar /var/www/html/backend/satgw-persist.jar

#echo "============= /cn-meta/build 创建归档目录 "history/$(date +"%Y-%m-%d")" =========="
#if [ ! -d "../history/"$(date +"%Y-%m-%d")"/" ];then
#  mkdir ../history/"$(date +"%Y-%m-%d")"
#  echo "create success"
#  else
#  echo "directory has exited"
#fi


#echo "================ 归档介质包到所创建的归档目录下 =========================="
#cp satgw-core-bootstrap/target/satgw-core-bootstrap-1.0.5-SNAPSHOT.jar ../history/"$(date +"%Y-%m-%d")"/"satgw-core-bootstrap-$(date +"%H-%M-%S").jar"
#echo "archive success"

echo "jar包下载地址：http://172.16.101.127/backend/satgw-persist.jar"