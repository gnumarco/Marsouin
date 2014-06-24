#! /usr/bin/python

# Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>.
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
# MA 02110-1301  USA

import json
import urllib2
import random

__author__="Marc Segond <dr.marc.segond@gmail.com>"
__date__ ="$Jun 24, 2014 1:51:25 PM$"

sensA = 1
sensB = 2

def calc(a, sA,sB):    
    return (sA*(2.75*a)+sB*-(0.35*a))

if __name__ == "__main__":
    jdata = '{"type":"config","values":[{"param":"pop.subpop.0.size","value":"2"},{"param":"generations","value":"3"}]}'
    print(jdata)
    urllib2.urlopen("http://127.0.0.1:8080/control", jdata)
    jdata = '{"type":"start"}'
    urllib2.urlopen("http://127.0.0.1:8080/control", jdata)
    
    for x in range(0, 10):
        for y in range(0, 10):
            sensA= random.random()
            sensB= random.random()
            jdata = '{"type":"sensors","values":[{"value":"'+str(sensA)+'"},{"value":"'+str(sensB)+'"}]}'
            res = urllib2.urlopen("http://127.0.0.1:8080/control", jdata)
            response = json.loads(res.next())
            print(response)
            matching = [s for s in response if "values" in s]
            act = response["values"][0]["value"]
            syst = calc(act, sensA, sensB)
            print(syst)
        fit = syst
        jdata = '{"type":"fitness","values":[{"value":"'+str(fit)+'"}]}'
        urllib2.urlopen("http://127.0.0.1:8080/control", jdata)