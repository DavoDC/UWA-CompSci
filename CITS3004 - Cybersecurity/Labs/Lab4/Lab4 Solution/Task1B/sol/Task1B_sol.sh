# length  = 64 hexadecimal characters
# Key = 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64

openssl dgst -hmac 1 -md5 plain.txt
openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64 -md5 plain.txt
openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64plus -md5 plain.txt
openssl dgst -hmac 1 -sha1 plain.txt
openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64 -sha1 plain.txt
openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64plus -sha1 plain.txt
openssl dgst -hmac 1 -sha256 plain.txt
openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64 -sha256 plain.txt
openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64plus -sha256 plain.txt

#openssl dgst -hmac 1 -md5 -out plain_md5_1.hash plain.txt
#openssl dgst -hmac ABCD -md5 -out plain_md5_ABCD.hash plain.txt
#openssl dgst -hmac 1 -sha1 -out plain_sha1.hash plain.txt
#openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64 -sha1 -out plain_sha1_64.hash plain.txt
#openssl dgst -hmac 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz64A -sha1 -out plain_sha1_64plus.hash plain.txt
#openssl dgst -hmac 1 -sha256 -out plain_sha256.hash plain.txt
#openssl dgst -hmac ABCD -sha256 -out plain_sha256_ABCD.hash plain.txt
