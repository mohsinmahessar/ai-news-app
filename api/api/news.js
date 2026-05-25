export default async function handler(req, res) {
  res.setHeader("Access-Control-Allow-Origin", "*");

  const key = process.env.GNEWS_KEY;
  const cat = req.query.cat || "world";

  const urls = {
    world:    `https://gnews.io/api/v4/top-headlines?category=world&lang=en&max=15&token=${key}`,
    pakistan: `https://gnews.io/api/v4/search?q=pakistan&lang=en&max=15&token=${key}`,
    tech:     `https://gnews.io/api/v4/top-headlines?category=technology&lang=en&max=15&token=${key}`,
    business: `https://gnews.io/api/v4/top-headlines?category=business&lang=en&max=15&token=${key}`,
    sports:   `https://gnews.io/api/v4/top-headlines?category=sports&lang=en&max=15&token=${key}`
  };

  try {
    const r = await fetch(urls[cat]);
    if (!r.ok) throw new Error("GNews error: " + r.status);
    const data = await r.json();
    res.status(200).json(data);
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
}
