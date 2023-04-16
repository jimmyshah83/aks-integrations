terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0.2"
    }
  }

  required_version = ">= 1.1.0"
}

provider "azurerm" {
  features {}
}

locals {
  resource_group_name = "rg-cc-redis-demo-01"
  location            = "canadacentral"
}

resource "azurerm_resource_group" "demo-rg" {
  name     = local.resource_group_name
  location = local.location
}

resource "azurerm_redis_cache" "demo-redis-cache" {
  name                = "redis-demo-01"
  resource_group_name = azurerm_resource_group.demo-rg.name
  location            = local.location
  dns_name            = "redis-demo-01"
  redis_version = "6.2"
  capacity            = 1
  family              = "C"
  sku_name            = "Basic"
  enable_non_ssl_port = false
  minimum_tls_version = "1.2"
}