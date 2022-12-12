 ### 0.1 - Versions

Currently, supports only one setup:

* **[Ansible_single]**: Single node. Created with Ansible for Microk8s (but prepared for other setups);

The provided setup was tested using `Ubuntu 20.04.3 LTS (Kernel: 5.4.0-97-generic) x86_64`, for all machines, and assumes the avaibility of *snap* packages.

## 1 - Requirements

* **[AnsibleSingle]** -> Setup controlled by Ansible. A `Controller` executes the *scripts* to configure a number of remote *hosts*:
    * *Controller* :
      * Ansible core >= 2.12.0:
      * Python >= 3 (including pip3);
      * sshpass >= 1.06;
      * rsync >= 3.1.0;
    * *Hosts*:
      * Python 3 (including pip3);
      * rsync >= 3.1.0 (same protocol version as *Controller*);
      * SSH Server on port 22;
      * A user account with permissions to run *snap* and write files in its home folder;

## 2 - Installation

* **[AnsibleSingle]**: after changing configurations to *inventory.yaml* (see Section 3), execute `deploy.sh` in *Controller* machine.
  * After setup, the admin should consider the following manual changes:
    * Add to each machine, in the `Node` information for `Kubernetes`, a tag in the pattern: `node.iotmapper/layer: <layer>`. With `layer` being either `input`, or, `exit`. These labels are obligatory to deploye applications, but are purposefly left as a manual configuration for greater control during testing. Most componenets won´t deploy if they are missing.
    * Register to each `IoTMapperLPWANReceiver` the used sensors. For `IoTMapper-LoRaWAN` an example can be seen in `example.json`.
    * Optionally, additional indexes may be added to the MongoDB databases used by the Orion Context Broker (OCB). While OCB, and MongoDB itself, adds some index, others may be required for scaling. See `https://fiware-orion.readthedocs.io/en/master/admin/perf_tuning.html#database-indexes`.       

## 3 - Configuration

* **[AnsibleSingle]** -> change fields as described below: 
  * {{ master_ip }}: IP for a `Host` machine that to be used as the main machine during setup;
  * {{ worker_ip }}: IP, or list of IPs, describing the workers nodes;
  * {{ ansible_username }}: username for a user account with most permissions (create files, install packages, etc);
  * {{ ansible_password_from_vault }}: password for a user account with most permissions (create files, install packages, etc). If unchaged will password from Ansible Vault dirs;
  * {{ frontend_ip }}: the IP to be used to server the frontend. Must be reachable by some external mechanism (e.g. OSPF).
