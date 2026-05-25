export default async function handler(req, res) {
  res.setHeader("Access-Control-Allow-Origin", "*");
  const { url } = req.query;
  if (!url) return res.status(400).json({ error: "No URL provided" });
  try {
    const response = await fetch(decodeURIComponent(url), {
      headers: { "User-Agent": "Mozilla/5.0" }
    });
    const text = await response.text();
    res.setHeader("Content-Type", "application/xml");
    res.send(text);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
}
