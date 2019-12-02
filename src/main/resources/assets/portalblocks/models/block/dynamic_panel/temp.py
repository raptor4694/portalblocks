import os
import re
from glob import iglob as glob
from itertools import chain, product, permutations
from textwrap import dedent
from collections import namedtuple
os.chdir(os.path.dirname(os.path.abspath(__file__)))

faces = ('north', 'south', 'east', 'west', 'up', 'down')
edges = ('top', 'left', 'right', 'bottom')
colors = ('white', 'black')

light_edges = ('lt', 'lb', 'rt', 'rb', 'tl', 'tr', 'bl', 'br')

for color in colors:
    for edge in light_edges:
        panel_face_name = f'{color}_light_{edge}'
        
        os.makedirs(panel_face_name, exist_ok=True)
        for face in faces:
            with open(f'{panel_face_name}/{face}.json', 'w') as file:
                file.write(dedent(f'''
                {{
                    "parent": "portalblocks:block/dynamic_panel/template_light_{face}_{edge}",
                    "textures": {{
                        "grid": "portalblocks:block/{color}/clean/grid"
                    }}
                }}''').lstrip())

# search = r'''"credit": "Made with Blockbench",'''
# replacement = r'''"credit": "Made with Blockbench",
# 	"parent": "block/block",'''

# for filename in glob('template_light_*.json'):
#     with open(filename, 'r') as file:
#         text = file.read()
#     text = text.replace(search, replacement)
#     with open(filename, 'w') as file:
#         file.write(text)

# namedata = namedtuple('namedata', ('name', 'texture'))

# panel_face_types = [
#     namedata(name='none', texture='frame_black'),
# ]

# for color in colors:
#     for variant in 'square', 'grid':
#         panel_face_types.append(namedata(name=f'{color}_{variant}', texture=f'panels/{color}/clean/{variant}'))
#     for face in 'top', 'left', 'right', 'bottom':
#         panel_face_types.append(namedata(name=f'{color}_tall_{face}', texture=f'panels/{color}/clean/tall_{face}'))
#     for face in 'lt', 'rt', 'lb', 'rb':
#         panel_face_types.append(namedata(name=f'{color}_large_{face}', texture=f'panels/{color}/clean/large_{face}'))

# for face in 'top', 'left', 'right', 'bottom':
#     panel_face_types.append(namedata(name=f'stained_white_tall_1_{face}', texture=f'panels/white/dirty/tall_1_{face}'))

# for panel_face_name, texture_loc in panel_face_types:
#     os.mkdir(panel_face_name)
#     for facing in faces:
#         with open(f"{panel_face_name}/{facing}.json", 'w') as file:
#             file.write(dedent(f'''
#             {{
#                 "parent": "portalblocks:block/dynamic_tile/template_{facing}",
#                 "textures": {{
#                     "face": "portalblocks:block/{texture_loc}"
#                 }}
#             }}''').lstrip())

