import os
import re
from glob import iglob as glob
from itertools import chain
from textwrap import dedent
from collections import namedtuple
from pyperclip import copy
os.chdir(os.path.dirname(os.path.abspath(__file__)))

faces = ('north', 'south', 'east', 'west', 'up', 'down')
edges = ('top', 'left', 'right', 'bottom')
light_edges = ('lt', 'lb', 'rt', 'rb', 'tl', 'tr', 'bl', 'br')
corners = ('lt', 'lb', 'rt', 'rb')
colors = ('white', 'black')

text = ''

for face in faces:
    Face = face[0].upper() + face[1:]
    text += f'''
	public TileEntityDynamicPanel set{Face}Face(EnumPanelFace type) {{
		if({face}Face != type) {{
			boolean recalcLight = {face}Face.isLight;
			{face}Face = type;
			if(recalcLight)
				recalcEmitsLight();
			else
				emitsLight |= type.isLight;
			updateBlock();
		}}
		return this;
    }}
    '''

copy(text)
print("Copied to clipboard!")

# import json

# regex = re.compile(r"_([bt][ns][ew])\.json$")

# face_names = {
#     'b': 'bottom',
#     't': 'top',
#     'n': 'north',
#     's': 'south',
#     'e': 'east',
#     'w': 'west'
# }

# faceInfoVars = {
#    f'#panel_{key}' : f'faceInfo{key.upper()}' for key in ('lt', 'rt', 'lb', 'rb')
# }
# faceInfoVars['#inside'] = 'faceInfoNone'

# face_lookup = {}
# for filename in glob("../models/block/template_large_panel_corner_*.json"):
#     m = regex.search(filename)
#     name = '_'.join(face_names[c] for c in m[1])
#     with open(filename, 'r') as file:
#         data = json.load(file)
#     face_lookup[name] = data['textures']

# text = ""

# for face, faces in face_lookup.items():
#     text += f"\n\t\t\tFACE_TYPE_LOOKUP.put(EnumLargePanelCorner.{face.upper()}, map = newEnumMap(EnumFacing.class));"
#     for face, val in faces.items():
#         text += f"\n\t\t\tmap.put(EnumFacing.{face.upper()+',':<6} {faceInfoVars[val]});"

# copy(text)
# print("Copied to clipboard!")

# panel_face_types = ['none']

# for color in colors:
#     for variant in 'square', 'grid':
#         panel_face_types.append(f'{color}_{variant}')
#     for edge in edges:
#         panel_face_types.append(f'{color}_tall_{edge}')
#     for corner in corners:
#         panel_face_types.append(f'{color}_large_{corner}')
#     for edge in light_edges:
#         panel_face_types.append(f'{color}_light_{edge}')

# for edge in edges:
#     panel_face_types.append(f'stained_white_tall_1_{edge}')
#     panel_face_types.append(f'light_both_{edge}')

# text = '''{
#     "multipart": ['''

# first = True

# for face in faces:
#     for panel_face_name in panel_face_types:
#         if first: first = False
#         else: text += ','
#         text += f'''
#         {{
#             "when": {{ "{face}": "{panel_face_name}" }},
#             "apply": {{ "model": "portalblocks:dynamic_panel/{panel_face_name}/{face}" }}
#         }}'''

# text += '''\
#     ]
# }'''

# with open('dynamic_panel.json', 'w') as file:
#     file.write(text)