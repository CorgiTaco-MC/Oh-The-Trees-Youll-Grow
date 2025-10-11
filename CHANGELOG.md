# 1.4.1
* Fix canopy and trunk additional block placements.

# 1.4.0
* Add sideways and upside down tree config choices.

# 1.3.14
* Make Feature Registration Methods Private

# 1.3.13
* Fix a crash relating to structure checks in tree growth.

# 1.3.12
* Concurrency safety for random scheduled ticks.

# 1.3.11
* Fix block check used in structure checks for tree growth.

# 1.3.10
* Add structure checks to tree growth during world generation.

# 1.3.9
* Add is sapling check. Fixes tree density issues.

# 1.3.8
* Fix trunks.

# 1.3.7
* Fix log decorators placing on leaves.
* Add more checks to fail tree growth.

# 1.3.6
* Fix placing tree decorator blocks.

# 1.3.5
* Rewrite tree growth logic to account for solid blocks and the surrounding environment prior to growing/placing.

# 1.3.4
* Track tree decoration positions when placing tree feature.

# 1.3.3
* Fix missing trunk/leave positions in tree decorators.

# 1.3.2
* Fix adding and sort trunk/leave positions and leaves to their respective sets. Fixes various issues with tree decorators.

# 1.3.1
* Add check to prevent logs from being placed where bedrock is present
* Fix TREE_FROM_NBT_V1 not being final
* Fix canopy log builders piercing terrain.

# 1.3.0
* Rewrite Build Script
* Generalize Registration
* Add TreeFromStructureNBTConfig constructor that has an empty tree decorator

# 1.2.0
* Move project to Arch Loom
* Add rotated block support
* Add example mushroom features

# 1.1.2
* Fix Tree Decorator Types not being registered on Fabric
* Use ForgeRegistries for Registration on Forge

# 1.1.1
* Fix incorrect Fabric version.

# 1.1.0
* Remove Regutils dependency

# 1.0.0
* Release