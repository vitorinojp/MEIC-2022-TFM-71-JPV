# IoTMapperCluster
Deployments for IoTMapper's clusters. 

Aggregates automation paths for creating logical clusters used in IoTMapper, including the  sub-cluster for Apache Kafka, FIWARE and the Presentation layer app.


### 0.1 - Versions

Currently, supports only one setup:

* **[Ansible_single]**: Single node. Created with Ansible for Microk8s (but prepared for other setups);

## 1 - Requirements

* **[AnsibleSingle]**:
    * *Controller* :
      * Ansible core >= 2.12.0:
      * Python >= 3 (including pip3);
      * sshpass >= 1.06;
      * rsync >= 3;
    * *Hosts*:
      * Python >= 3 (including pip3);
      * rsync >= 3 (same protocol version as *Controller*);
      * SSH Server;
      * A user account with permissions to run *snap* and write files in its home folder;

## 2 - Installation

* **[AnsibleSingle]**: as needed change configurations (hosts list and user!) in *inventory.yaml*, then execute deploy.sh in *Controller*.

## 3 - Configuration

* **[AnsibleSingle]**: *inventory.yaml*, change *ansible_user* and *all.hosts*. *group_vars.all.vault*, create an Ansible Vault file (*vault*) with: *ansible_password_from_vault: <ansible_user_password>*;