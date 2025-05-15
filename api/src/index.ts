import express from 'express';
import crypto from 'crypto';
import qrcode from 'qrcode-terminal';

interface SeedResponse {
  readonly seed: string;
  readonly expires_at: string;
 }

const app = express();
const PORT = 3000;

let lastGeneratedSeed: SeedResponse | null = null;

app.get('/seed', (_req, res) => {
  const seed = crypto.randomBytes(16).toString('hex'); // 32-character hex string
  const expiresAt = new Date(Date.now() + 5 * 60 * 1000).toISOString(); // expires in 5 minutes
  const response: SeedResponse = { seed, expires_at: expiresAt };

  res.status(200).json(response);
});

app.get('/seed_test', (_req, res) => {
  const seed = crypto.randomBytes(16).toString('hex');
  const expiresAt = new Date(Date.now() + 5 * 60 * 1000).toISOString();

  // Save to local memory
  lastGeneratedSeed = { seed, expires_at: expiresAt };

  // Generate and display QR code in terminal
  console.log('\nGenerated Seed QR Code:\n');
  qrcode.generate(seed, { small: true });
  console.log(`\nExpires at: ${expiresAt}\n`);

  res.status(200).send(`Seed generated and QR code displayed in terminal. Expires at: ${expiresAt}`);
});

app.listen(PORT, () => {
  console.log(`Server is running at http://localhost:${PORT}`);
});
