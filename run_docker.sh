#!/bin/bash
# Construir la imagen
docker build -t miapp:latest .

# Ejecutar la aplicación en un contenedor temporal
docker run --rm miapp:latest
