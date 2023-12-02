const express = require('express');
const app = express();
const port = 3000;

// Rota simples que retorna a string "Estou funcionando"
app.get('/request', (req, res) => {
  // Obtém a taxa de falha dos query parameters, se presente
  const queryFailureRate = parseFloat(req.query.failureRate);

  // Usa a taxa de falha dos query parameters se presente, senão, utiliza da variável de ambiente ou o valor padrão
  const failureRate = !isNaN(queryFailureRate) ? queryFailureRate : parseFloat(process.env.FAILURE_RATE) || 0.1;

  // Simula a falha com base na taxa de falha configurada
  const requestFail = Math.random() < failureRate;
  const statusCode = ( requestFail ? 500 : 200);
  res.status(statusCode).send(`Request status: ${requestFail ? "failed" : "successed"}! Current fail rate: ${failureRate}`);
});

// Inicia o servidor na porta especificada
app.listen(port, () => {
  console.log(`Server is running in http://localhost:${port}!`);
});

// Trata o sinal SIGINT (Ctrl+C) para encerrar o servidor de forma adequada
process.on('SIGINT', () => {
  console.info("Interrupted");
  process.exit(0);
});
