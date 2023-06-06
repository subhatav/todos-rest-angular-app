
# Cache the Dependencies

FROM node:18.16.0-alpine AS builder

WORKDIR /todos-app
COPY package.json package-lock.json ./

RUN npm install

# Build the Application

COPY . .
RUN npm run build

# Import the Resources

FROM nginx:1.25.0-alpine AS runner

LABEL version="1.0.0"
LABEL author="Ph4nToM"
LABEL maintainer="subhatav@gmail.com"

COPY /nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /todos-app/dist/todos-angular-frontend /usr/share/nginx/html
