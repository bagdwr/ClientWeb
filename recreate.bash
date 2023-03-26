docker rm -f project-pg

docker volume rm clientweb_pg_volume

docker-compose down

docker-compose up -d
