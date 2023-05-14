terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = ">= 3.7.0"
    }
  }

  # Needs to be pre-configured in Azure either manually or via another Terraform script
  backend "azurerm" {
    resource_group_name  = "rg-tf-github-actions-state"
    storage_account_name = "tfgithubactions01"
    container_name       = "tfstate"
    key                  = "terraform.tfstate"
    use_oidc             = true
  }
}

provider "azurerm" {
  features {}
  use_oidc = true
  skip_provider_registration = "true"
}

locals {
  resource_group_name = "rg-cc-gh-demo"
  location            = "canadacentral"
}

resource "azurerm_resource_group" "rg-cc-gh-demo" {
  name     = local.resource_group_name
  location = local.location
}
# Redis Cache + AKS for ths deployment
#resource "azurerm_redis_cache" "demo-redis-cache" {
#  name                = "redis-demo-01"
#  resource_group_name = azurerm_resource_group.demo-rg.name
#  location            = local.location
#  dns_name            = "redis-demo-01"
#  redis_version       = "6.2"
#  capacity            = 1
#  family              = "C"
#  sku_name            = "Basic"
#  enable_non_ssl_port = false
#  minimum_tls_version = "1.2"
#}