echo "1. Mine GET Request:" & curl -o mine.json -s http://127.0.0.1:5000/mine
cat mine.json
echo ""
echo "2. verifyPOW for Block Index 2 (it doesnt work for other indexes):" & python verifyPOW.py
echo ""
echo "3. Create a New Transaction:"
curl -X POST \
       -H "Content-Type: application/json" \
       -d '{"sender":"d4ee26eee15148ee92c6cd394edd974e", "recipient":"someone-other address", "amount":5}' \
       http://127.0.0.1:5000/transactions/new
echo ""
echo "4. Inspect A Chain:" & curl -o chain.json -s http://127.0.0.1:5000/chain
cat chain.json
