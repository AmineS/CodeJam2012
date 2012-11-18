import subprocess
from multiprocessing import Process
import time

browser = "chromium-browser"
url = "www.google.com"

def main():
	# launch ms exchange
	msP = Process(target=runMSExchange)
	msP.start()

	# wait for it to get ready...
	time.sleep(1)

	# run Mset
	msetP = Process(target=runMSET)
	#gui = Process(target=runGUI)
	msetP.start()
	#gui.start()

	msetP.join()
	#gui.join()
	msP.join()

def runMSExchange():
	# run the ms exchange
	subprocess.call(["java", "-jar", "msExchange.jar", "-p", "3000", "-t", "3001", "-d", "data/amd.txt"])

def runMSET():
	# run our program
	subprocess.call(["java", "Mset"])

def runGUI():
	# run GUI 	
	subprocess.call([browser, url])
	
if __name__=="__main__":
	main()
