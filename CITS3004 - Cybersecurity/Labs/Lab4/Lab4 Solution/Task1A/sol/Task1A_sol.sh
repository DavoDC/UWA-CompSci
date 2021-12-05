openssl dgst -md5 plain.txt
openssl dgst -sha1 plain.txt
openssl dgst -sha256 plain.txt
openssl dgst -whirlpool plain.txt

openssl dgst -md5 -out plain_md5.hash plain.txt
openssl dgst -sha1 -out plain_sha1.hash plain.txt
openssl dgst -sha256 -out plain_sha256.hash plain.txt
openssl dgst -whirlpool -out plain_whirlpool.hash plain.txt
