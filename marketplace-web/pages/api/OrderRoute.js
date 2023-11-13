
export default function handler(req, res) {
  const reqMethod = req.method;
  if (reqMethod === 'POST') {
    const flag = req.query.flag;
    const transactionId = req.query.txid;
    // return res.redirect(307,`http://localhost:3000/account/orders?flag=${flag}&txid=${transactionId}`);
    // return res.redirect(307,`https://nuvio.in/account/orders?flag=${flag}&txid=${transactionId}`);
    //res.status(200).json({ message: 'This is a POST request' });
    res.writeHead(302, { Location: `/account/orders?flag=${flag}&txid=${transactionId}` });
    res.end();
  } else {
    // For any other method, return a 405 Method Not Allowed status code
    res.setHeader('Allow', ['POST']);
    res.status(405).end(`Method ${req.method} Not Allowed`);
  }
}