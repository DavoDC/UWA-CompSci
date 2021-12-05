echo "1. Register Node 2 with Node 1"
curl -X POST \
       -H "Content-Type: application/json" \
       -d '{"nodes":["127.0.0.1:5001"]}' \
       http://127.0.0.1:5000/nodes/register
echo ""
echo "2. Register Node 1 with Node 2"
curl -X POST \
       -H "Content-Type: application/json" \
       -d '{"nodes":["127.0.0.1:5000"]}' \
       http://127.0.0.1:5001/nodes/register
echo ""
echo "3.1 Mine GET Request on Node 1:" & curl -o temp_mine.json -s http://127.0.0.1:5000/mine
cat temp_mine.json
echo ""
echo "3.2 Mine GET Request on Node 1:" & curl -o temp_mine.json -s http://127.0.0.1:5000/mine
cat temp_mine.json
echo ""
echo "4.1 Mine GET Request on Node 2:" & curl -o temp_mine.json -s http://127.0.0.1:5001/mine
cat temp_mine.json
echo ""
echo "4.2 Mine GET Request on Node 2:" & curl -o temp_mine.json -s http://127.0.0.1:5001/mine
cat temp_mine.json
echo ""
echo "4.3 Mine GET Request on Node 2:" & curl -o temp_mine.json -s http://127.0.0.1:5001/mine
cat temp_mine.json
echo ""
echo "4.4 Mine GET Request on Node 2:" & curl -o temp_mine.json -s http://127.0.0.1:5001/mine
cat temp_mine.json
echo ""
echo "5. Inspect The Chain on Node 1:" & curl -o temp_chain.json -s http://127.0.0.1:5000/chain
cat temp_chain.json
echo ""
echo "6. Resolve GET on Node 1:" & curl -o temp_resolve.json -s http://127.0.0.1:5000/nodes/resolve
cat temp_resolve.json
echo ""
