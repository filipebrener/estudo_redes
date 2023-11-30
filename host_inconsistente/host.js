require('dotenv').config();

const express = require('express');
const app = express();
const port = 3000;

// Obtem a taxa de falha da variável de ambiente ou define um valor padrão de 0.1 (10% de taxa de falha)
const failureRate = parseFloat(process.env.FAILURE_RATE) || 0.0;

// Rota simples que retorna a string "Estou funcionando"
app.get('/request', (req, res) => {
  // Simula a falha com base na taxa de falha configurada
  if (Math.random() < failureRate) {
    res.status(500).send('Erro interno do servidor');
  } else {
    res.send('Estou funcionando');
  }
});

// Inicia o servidor na porta especificada
app.listen(port, () => {
  console.log(`Server is running in http://localhost:${port}! failureRate: ${failureRate}`);
});

// Trata o sinal SIGINT (Ctrl+C) para encerrar o servidor de forma adequada
process.on('SIGINT', () => {
  console.info("Interrupted");
  process.exit(0);
});
