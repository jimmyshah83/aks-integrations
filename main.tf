# Generate random resource group name
resource "random_pet" "rg_name" {
  prefix = var.resource_group_name_prefix
}

resource "azurerm_resource_group" "rg_cc_gh_demo" {
  location = var.location
  name     = random_pet.rg_name.id
}

resource "azurerm_kubernetes_cluster" "aks_gh_demo" {
  location            = azurerm_resource_group.rg_cc_gh_demo.location
  name                = var.cluster_name
  resource_group_name = azurerm_resource_group.rg_cc_gh_demo.name
  dns_prefix          = var.dns_prefix
  tags = {
    Environment = "Production"
  }

  default_node_pool {
    name       = "agentpool"
    vm_size    = "Standard_D2_v2"
    node_count = var.agent_count
  }

  identity {
    type = "SystemAssigned"
  }

  network_profile {
    network_plugin    = "kubenet"
    load_balancer_sku = "standard"
  }
}

# NOTE: the Name used for Redis needs to be globally unique
resource "azurerm_redis_cache" "redis_gh_demo" {
  name                = "gh-demo"
  location            = azurerm_resource_group.rg_cc_gh_demo.location
  resource_group_name = azurerm_resource_group.rg_cc_gh_demo.name
  capacity            = 2
  family              = "C"
  sku_name            = "Standard"
  enable_non_ssl_port = false
  minimum_tls_version = "1.2"

  redis_configuration {
  }
}

# Create an ACR
resource "azurerm_container_registry" "acr_gh_demo" {
  name                = "acraksghdemo"
  resource_group_name = azurerm_resource_group.rg_cc_gh_demo.name
  location            = azurerm_resource_group.rg_cc_gh_demo.location
  sku                 = "Premium"
}

Attach it to the cluster
resource "azurerm_role_assignment" "acr_aks_role_assignment_gh_demo" {
  principal_id                     = azurerm_kubernetes_cluster.aks_gh_demo.kubelet_identity[0].object_id
  role_definition_name             = "AcrPull"
  scope                            = azurerm_container_registry.acr_gh_demo.id
  skip_service_principal_aad_check = true
}