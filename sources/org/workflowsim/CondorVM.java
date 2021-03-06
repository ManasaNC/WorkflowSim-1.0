/**
 * Copyright 2012-2013 University Of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.workflowsim;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;
import org.workflowsim.utils.ReplicaCatalog;
import org.workflowsim.utils.ReplicaCatalog.FileSystem;

/**
 * Condor Vm extends a VM: the difference is it has a locl storage system and it
 * has a state to indicate whether it is busy or not
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class CondorVM extends Vm {

    /*
     * The local storage system a vm has if file.system=LOCAL
     */
    private ClusterStorage storage;
    /*
     * The state of a vm. It should be either WorkflowSimTags.VM_STATUS_IDLE
     * or VM_STATUS_READY (not used in workflowsim) or VM_STATUS_BUSY
     */
    private int state;

    /**
     * Creates a new CondorVM object.
     *
     * @param id unique ID of the VM
     * @param userId ID of the VM's owner
     * @param mips the mips
     * @param numberOfPes amount of CPUs
     * @param ram amount of ram
     * @param bw amount of bandwidth
     * @param size amount of storage
     * @param vmm virtual machine monitor
     * @param cloudletScheduler cloudletScheduler policy for cloudlets
     * @pre id >= 0
     * @pre userId >= 0
     * @pre size > 0
     * @pre ram > 0
     * @pre bw > 0
     * @pre cpus > 0
     * @pre priority >= 0
     * @pre cloudletScheduler != null
     * @post $none
     */
    public CondorVM(
            int id,
            int userId,
            double mips,
            int numberOfPes,
            int ram,
            long bw,
            long size,
            String vmm,
            CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
        /*
         * At the beginning all vm status is idle. 
         */
        setState(WorkflowSimTags.VM_STATUS_IDLE);
        /*
         * If the file.system is LOCAL, we should add a clusterStorage to vm. 
         */
        if (ReplicaCatalog.getFileSystem() == FileSystem.LOCAL) {
            try {
                storage = new ClusterStorage(Integer.toString(id), 1e6);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Sets the state of the task
     *
     * @param type the type
     * @return $none
     */
    public final void setState(int tag) {
        this.state = tag;
    }

    /**
     * Gets the state of the task
     *
     * @return the state of the task
     * @pre $none
     * @post $none
     */
    public final int getState() {
        return this.state;
    }

    /**
     * Adds a file to the local file system
     *
     * @param file to file to be added to the local
     * @pre $none
     * @post $none
     */
    public void addLocalFile(org.cloudbus.cloudsim.File file) {
        if (this.storage != null) {
            this.storage.addFile(file);
        } else {
        }
    }

    /**
     * Removes a file from the local file system
     *
     * @param file to file to be removed to the local
     * @pre $none
     * @post $none
     */
    public void removeLocalFile(org.cloudbus.cloudsim.File file) {
        if (this.storage != null) {
            this.storage.deleteFile(file);
        }
    }

    /**
     * Tells whether a file is in the local file system
     *
     * @return whether the file exists in the local file system
     * @pre $none
     * @post $none
     */
    public boolean hasLocalFile(org.cloudbus.cloudsim.File file) {
        if (this.storage != null) {
            return this.storage.contains(file);
        }
        return false;

    }
}
