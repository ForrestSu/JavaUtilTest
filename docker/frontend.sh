port=50031:80
name=micro-frontend
image_name=192.168.8.192:5000/fms/micro-frontend
docker pull $image_name
if test `docker ps -f "name=$name" -a -q` ; then
    echo "已经存在容器,移除该容器"
    docker rm -f -v $name
fi

docker run -d --name $name -p $port $image_name

echo "启动容器成功"