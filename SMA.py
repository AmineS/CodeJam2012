#!/usr/bin/python

# Import modules for CGI handling 
import cgi, cgitb 
import json

def getPointer():
	f = open("SMAPointer.txt",r)
	lines = f.readlines()
	f.close()
	f = open("SMAPointer.txt",w)
	f.write(lines[0].split(' ')[0] + 1)
	f.close()
	return lines[0].split(' ')[0]

def line_to_json(line):
	split_l = line.rstrip('\n').split(' ')
	return json.dumps({'tick':(split_l[0]), 'slow':(split_l[1]), 'fast':(split_l[2])})

def getLine():
	fp = open("SMA.txt")
	for i, line in enumerate(fp):
		if (i=getPointer())
			return line_to_json(line)

return getLine()
