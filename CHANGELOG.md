# 10.1.0
* Match 1.21.1 branch
* Add the ability to define the log filter behavior. Filter options include: `PIERCE` - Destroy blocks in the filter. `PASSTHROUGH` - Tree will generate but skip blocks in filter. `BLOCK` - Tree will not generate if it meets a block in the filter.


# 10.0.4
* Fix default log placement filter.

# 10.0.3
* Use the correct filter when checking for valid tree growth positions.

# 10.0.2
* Internal fixes

# 10.0.1
* Add back forge support.

# 10.0.0
* Update to 26.1.

# 5.3.0
* Add the ability to turn on/off random tree rotation. 

# 5.2.1
* Fix issue with piercing walls and structures and bedrock

# 5.2.0
* Add the ability to use blocks within the NBT to place Blockstates via Blockstate providers.
* Allow the ability to specify multiple leave targets and multiple leave block state providers.
* Introduce Tree From Structure NBT v2
* Fix placing additional blocks from NBT in the v1 feature

# 5.1.3
* Allow BlockStateProperties.FACING to be used on AttachedToLogsDecorator and still set Direction correctly.

# 5.1.2
* Fix trunk positions.

# 5.1.1
* Fix canopy and trunk additional block placements.

# 5.1.0
* Add sideways and upside down tree config choices.

# 5.0.14
* Concurrency safety for random scheduled ticks.

# 5.0.13
* Fix crash pertaining to calling chunks out of range.

# 5.0.12
* Add structure checks to tree growth during world generation.

# 5.0.11
* Add is sapling check. Fixes tree density issues.

# 5.0.10
* Fix trunks.

# 5.0.9
* Fix log decorators placing on leaves.
* Add more checks to fail tree growth.

# 5.0.8
* Fix placing tree decorator blocks.

# 5.0.7
* Rewrite tree growth logic to account for solid blocks and the surrounding environment prior to growing/placing.

# 5.0.6
* Track tree decoration positions when placing tree feature.

# 5.0.5
* Fix missing trunk/leave positions in tree decorators.

# 5.0.4
* Fix adding and sort trunk/leave positions and leaves to their respective sets. Fixes various issues with tree decorators.

# 5.0.3
* Fix canopy log builders piercing terrain

# 5.0.2
* Fix NeoForge Jar Missing AccessTransformer
* Make TREEFROMSTRUCTURENBT final
* Fix Deprecation of FMLJavaModLoadingContext.get in forge
* Adjust some Publishing Names

# 5.0.1
* Add check to prevent logs from being placed where bedrock is present

# 5.0.0
* Update to 1.21.1

# 4.0.0
* Update to 1.20.6
* Move Datagen to NeoForge

# 3.0.0
* Update to 1.20.4

# 2.0.0
* Update to 1.20.2
* Add NeoForge Support
* Remove CorgiLib Legacy Support

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