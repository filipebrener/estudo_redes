# Estágio de construção
FROM node:20 AS builder

WORKDIR /app

# Copia apenas os arquivos relacionados ao pacote (package.json e package-lock.json) para aproveitar o cache do Docker
COPY package*.json ./

# Instala as dependências
RUN npm install

# Copia o código-fonte da aplicação
COPY . .

# Estágio de produção
FROM node:20

WORKDIR /app

# Copia os arquivos necessários do estágio de construção
COPY --from=builder /app .

# Comando para executar a aplicação quando o contêiner iniciar
CMD ["node", "host.js"]