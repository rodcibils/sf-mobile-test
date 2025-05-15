import express from 'express';
import crypto from 'crypto';

const app = express();
const PORT = 3000;

app.get('/seed', (_req, res) => {
  const seed = crypto.randomBytes(16).toString('hex'); // 32-character hex string
  const expiresAt = new Date(Date.now() + 5 * 60 * 1000).toISOString(); // expires in 5 minutes

  res.status(200).json({
    seed,
    expires_at: expiresAt,
  });
});

app.listen(PORT, () => {
  console.log(`Server is running at http://localhost:${PORT}`);
});
