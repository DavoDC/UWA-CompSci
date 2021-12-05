steghide embed -cf linux.jpg -ef secret.txt -sf stego.jpg -p MyPassword
steghide extract -sf stego.jpg -p MyPassword -xf extractedSecret.txt
cat extractedSecret.txt
