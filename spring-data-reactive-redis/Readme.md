## Reactive Redis using Spring Data Redis

This project is a sample application that shows how to use Spring Data Redis to build a reactive application.

### Prerequisites

* JDK 17 or later
* Gradle 6+ or later
* Redis 6.0.0 or later
* Docker

### KeyVault
1. App needs to have the correct access policy to access the KeyVault  
   https://learn.microsoft.com/en-us/azure/key-vault/general/assign-access-policy?tabs=azure-portal

### Terraform

1. az login

 ```
[
   {
   "cloudName": "AzureCloud",
   "homeTenantId": "<TENANT_ID>",
   "id": "<SUBSCRIPTION_ID>",
   "isDefault": true,
   "managedByTenants": [],
   "name": "<NAME>",
   "state": "Enabled",
   "tenantId": "<TENANT_ID>",
   "user": {
        "name": "<NAME>",
        "type": "user"
        }
   }
   ]
```

2. az account set --subscription "<Subscription ID>"
3. az ad sp create-for-rbac --role="Contributor" --scopes="/subscriptions/<SUBSCRIPTION_ID>"

```{
   "appId": "<CLIENT_ID>",
   "displayName": "<DISPLAY_NAME>",
   "password": "<PASSWORD>",
   "tenant": "<TENANT_ID>"
   }
   ```

4. Set environment variables

```
 export ARM_CLIENT_ID="<APPID_VALUE>"
 export ARM_CLIENT_SECRET="<PASSWORD_VALUE>"
 export ARM_SUBSCRIPTION_ID="<SUBSCRIPTION_ID>"
 export ARM_TENANT_ID="<TENANT_VALUE>"
```

5. Create Resources

- Resource Group
- Redis Cache
- ACR
- AKS
- Provide AKS access to pull images from ACR
