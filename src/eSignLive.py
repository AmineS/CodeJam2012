import os,json

p = os.popen('curl -X "POST" -H "Authorization: Basic Y29kZWphbTpBRkxpdGw0TEEyQWQx" -H "Content-Type:application/json" --data-binary @codejam.json "https://stage-api.e-signlive.com/aws/rest/services/codejam"').read()

result = json.loads(p)
print p
print result['ceremonyId']
