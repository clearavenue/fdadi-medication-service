docker build -t clearavenue/fdadi-medication-service .
docker run --rm -it --name fdadi-medication-service --net fdadi-bridge -p 8083:8083 clearavenue/fdadi-medication-service

