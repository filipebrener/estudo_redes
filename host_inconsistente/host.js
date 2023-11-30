const express = require('express');
const app = express();
const port = 3000;

// Rota simples que retorna a string "Estou funcionando"
app.get('/healthCheck', (req, res) => {
  res.send('Estou funcionando');
});

// Inicia o servidor na porta especificada
app.listen(port, () => {
  console.log(`Servidor está rodando em http://localhost:${port}`);
});