# ----- Etapa 1: Build -----
FROM node:25-alpine AS build

WORKDIR /app

ARG VITE_API_GATEWAY_URL
ENV VITE_API_GATEWAY_URL=$VITE_API_GATEWAY_URL

# Solo copiar package.json y package-lock.json primero (cache eficiente)
COPY package*.json ./

RUN npm install

# Ahora copia el resto del proyecto
COPY . .

# Construye tu frontend
RUN npm run build

# ----- Etapa 2: Servir archivos estáticos con Nginx -----
FROM nginx:alpine

# Copiar los archivos construidos de "build" o "dist"
COPY --from=build /app/dist /usr/share/nginx/html
COPY --from=build /app/nginx.conf /etc/nginx/conf.d/default.conf
# Para React CRA sería: /app/build

# Exponer el puerto
EXPOSE 8050

CMD ["nginx", "-g", "daemon off;"]
