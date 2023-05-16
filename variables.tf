variable "agent_count" {
  default = 3
}

variable "cluster_name" {
  default = "aksghdemo"
}

variable "dns_prefix" {
  default = "aksghdemo"
}

variable "location" {
  default     = "canadacentral"
  description = "Location for all resources on Azure"
}

variable "resource_group_name_prefix" {
  default     = "rg"
  description = "Prefix of the resource group name that's combined with a random ID so name is unique in your Azure subscription."
}