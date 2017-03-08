sudo kill $(sudo lsof -i:443)
git pull --prune
mvn clean package -Dmaven.test.skip.exec
sudo nohup java -jar target/loto-data-api-1.0.0.jar &
