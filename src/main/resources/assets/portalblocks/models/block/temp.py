import os
import re
from glob import iglob as glob
from itertools import chain, filterfalse
from collections import namedtuple
from textwrap import dedent
os.chdir(os.path.dirname(os.path.abspath(__file__)))

faces = ('north', 'south', 'east', 'west', 'up', 'down')
colors = ('white', 'black')

namedata = namedtuple('namedata', ('mdlprefix', 'mdlsuffix', 'texprefix', 'texsuffix'), defaults=('', '', '', ''))

variants = {
    'white': {
        'clean': [namedata()],
        'dirty': [namedata(mdlprefix='stained_', mdlsuffix='_1', texsuffix='_1')]
    },
    'black': {
        'clean': [namedata()],
        'dirty': []
    }
}

insides = {
    'white': 'frame_black',
    'black': 'frame_yellow'
}

for color in colors:
    color_variants = variants[color]
    inside = insides[color]
    for face in faces:
        for variant, namedatas in color_variants.items():
            for mdlprefix, mdlsuffix, texprefix, texsuffix in namedatas:
                with open(f"{mdlprefix}{color}_tall_panel{mdlsuffix}_{face}.json", 'w') as file:
                    file.write(dedent(f'''
                    {{
                        "parent": "portalblocks:block/template_tall_panel_{face}",
                        "textures": {{
                            "square": "portalblocks:block/panels/{color}/clean/square",
                            "inside": "portalblocks:block/{inside}",
                            "left": "portalblocks:block/panels/{color}/{variant}/{texprefix}tall{texsuffix}_left",
                            "right": "portalblocks:block/panels/{color}/{variant}/{texprefix}tall{texsuffix}_right",
                            "top": "portalblocks:block/panels/{color}/{variant}/{texprefix}tall{texsuffix}_top",
                            "bottom": "portalblocks:block/panels/{color}/{variant}/{texprefix}tall{texsuffix}_bottom"
                        }}
                    }}''').lstrip())

# regex = re.compile(r'"portalblocks:block/frame_(black|yellow)"')

# def replace(is_white: bool):
#     def replace(match: re.Match):
#         if match[1] == "yellow" and is_white:
#             return '"portalblocks:block/frame_black"'
#         else:
#             return '"portalblocks:block/frame_yellow"'
#     return replace

# def is_template(fname: str):
#     return fname.startswith('template_')

# filenames = chain(
#     filterfalse(is_template, glob('*_large_panel_corner_*.json')),
#     filterfalse(is_template, glob('*_tall_panel_*.json')),
# )

# for filename in filenames:
#     with open(filename, 'r') as file:
#         text = file.read()
#     text = regex.sub(replace('white' in filename), text)
#     with open(filename, 'w') as file:
#         file.write(text)    
    