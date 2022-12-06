#!/bin/sh

export ANSIBLE_HOST_KEY_CHECKING=False
ansible-playbook iotmapper_deploy.yaml -i inventory.yaml --ask-vault-pass "$@"