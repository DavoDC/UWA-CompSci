
START=70
END=90

for ((i=START; i<=END; i++)); do
    python3 gen_payload.py $i
    ./run.sh
    echo ""
done
