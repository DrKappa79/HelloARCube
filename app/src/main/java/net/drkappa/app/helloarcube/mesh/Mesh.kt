package net.drkappa.app.helloarcube.mesh

import net.drkappa.app.helloarcube.parameters.InstanceData


abstract class Mesh {

    var instanceData = InstanceData()

    abstract fun load()

    abstract fun draw()

    abstract fun cloneDrawData() : Mesh


}