docker compose down --rmi all

docker build -t pollsystem .

docker compose up --build --force-recreate --remove-orphans