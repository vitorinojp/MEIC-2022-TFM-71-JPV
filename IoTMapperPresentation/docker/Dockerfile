FROM node:16 AS builder

ARG REACT_APP_ENV
ENV REACT_APP_ENV=${REACT_APP_ENV}

WORKDIR /opt/iotmapperpresentation/builder

COPY tsconfig.json ./
COPY package*.json ./

RUN npm install --ignore-scripts
RUN npm install typescript@4.3.5
RUN npm install react-scripts -g

COPY public/ ./public
COPY src/ ./src

RUN NODE_ENV=production REACT_APP_ENV=$REACT_APP_ENV npm run build

FROM node:16

WORKDIR /opt/iotmapperpresentation/

COPY --from=builder /opt/iotmapperpresentation/builder/build .

RUN npm install serve -g --silent

ARG PORT
ENV PORT=${PORT}

EXPOSE 8080
EXPOSE 80

CMD [ "serve", "-s", "/opt/iotmapperpresentation/" ]
