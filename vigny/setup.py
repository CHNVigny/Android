from distutils.core import setup 
import py2exe
opts = { "py2exe": { "includes": "xml.etree.ElementTree, urllib2, bs4, feedparser" } }
# setup( options = opts, name="china.exe", version="0.1", console=["china.py"])
setup( options = opts, name="finance.exe", version="0.1", console=["finance.py"])
# setup( options = opts, name="js.exe", version="0.1", console=["js.py"])
# setup( options = opts, name="social.exe", version="0.1", console=["social.py"])
# setup( options = opts, name="sports.exe", version="0.1", console=["sports.py"])
# setup( options = opts, name="tech.exe", version="0.1", console=["tech.py"])
# setup( options = opts, name="world.exe", version="0.1", console=["world.py"])