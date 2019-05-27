FROM node:12.3-alpine AS builder
COPY . ./explorer-ng
WORKDIR /explorer-ng
RUN npm i
RUN $(npm bin)/ng build --prod

FROM nginx:1.15.8-alpine
COPY --from=builder /explorer-ng/dist /usr/share/nginx/html
