#!/bin/bash

docker_setup_env(){
  file_env 'DB_URL'
  file_env 'DB_USER'
  file_env 'DB_PASS'
  file_env 'JWT_TOKEN'
}

file_env(){
	local var="$1"
	local fileVar="/run/secrets/$var"
	local def="${2:-}"
	if [ "${!var:-}" ] && [ -f "$fileVar" ]; then
		echo "Both $var and $fileVar are set"
		exit 1
	fi
	local val="$def"
	if [ "${!var:-}" ]; then
		val="${!var}"
	elif [ -f "$fileVar" ]; then
		val="$(< "$fileVar")"
	fi
	export "$var"="$val"
}

# Call the docker_setup_env function to set the environment variables
docker_setup_env

# Start the Spring Boot application using exec to replace the shell with the java process
exec java org.springframework.boot.loader.launch.JarLauncher
