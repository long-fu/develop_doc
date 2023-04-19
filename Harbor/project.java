package com.iot.teio.harbor;

import com.google.gson.reflect.TypeToken;
import com.iot.teio.harbor.models.*;

import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.ApiResponse;
import io.kubernetes.client.openapi.Pair;

import io.kubernetes.client.openapi.models.V1Status;
//import io.kubernetes.client.util.credentials.Authentication;
import io.kubernetes.client.util.credentials.ClientCertificateAuthentication;
import io.kubernetes.client.util.credentials.KubeconfigAuthentication;
//import io.kubernetes.client.util.credentials.UsernamePasswordAuthentication;
import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.*;

public class CoreV1Api {
    private ApiClient localVarApiClient;
    private List<Protocol> protocols = Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1);
    private Duration pingInterval = Duration.ofMinutes(1);
    private Duration readTimeout = Duration.ZERO;

    private String basePath;

    private Boolean verifyingSsl = false;

    private String keyStorePassphrase;
    public CoreV1Api() {
        String username = "admin";
        String password = "Harbor12345";

        basePath = "https://119.23.231.199/api/v2.0";

        ApiClient client = new ApiClient();
//        authentications
//        client.setUsername();
        client.setUsername(username);
        client.setPassword(password);
        client.setHttpClient(
                client
                        .getHttpClient()
                        .newBuilder()
                        .protocols(protocols)
                        .readTimeout(this.readTimeout)
                        .pingInterval(pingInterval)
                        .build());

        if (basePath != null) {
            if (basePath.endsWith("/")) {
                basePath = basePath.substring(0, basePath.length() - 1);
            }
            client.setBasePath(basePath);
        }


        client.setVerifyingSsl(verifyingSsl);

        localVarApiClient = client;

    }

    // MARK: 创建Project
    public Call createProjectCall(V1Project body, ApiCallback _callback) throws ApiException {
        String localVarPath = "/projects";
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, body, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call createProjectValidateBeforeCall(V1Project body, ApiCallback _callback) throws ApiException {
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createNamespace(Async)");
        } else {
            Call localVarCall = this.createProjectCall(body, _callback);
            return localVarCall;
        }
    }

    public V1Project createProject(V1Project body, String pretty, String dryRun, String fieldManager) throws ApiException {
        ApiResponse<V1Project> localVarResp = this.createProjectWithHttpInfo(body);
        return (V1Project) localVarResp.getData();
    }

    public ApiResponse<V1Project> createProjectWithHttpInfo(V1Project body) throws ApiException {
        Call localVarCall = this.createProjectValidateBeforeCall(body, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call createProjectAsync(V1Project body, ApiCallback<V1Project> _callback) throws ApiException {
        Call localVarCall = this.createProjectValidateBeforeCall(body, _callback);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 删除Project
    public Call deleteProjectCall(String name, ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;
//        /projects/{project_name_or_id}
        String localVarPath = "/projects/{name}".replaceAll("\\{name\\}", this.localVarApiClient.escapeString(name.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call deleteProjectValidateBeforeCall(String name, ApiCallback _callback) throws ApiException {
        if (name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else {
            Call localVarCall = this.deleteProjectCall(name, _callback);
            return localVarCall;
        }
    }

    public V1Status deleteProject(String name) throws ApiException {
        ApiResponse<V1Status> localVarResp = this.deleteProjectWithHttpInfo(name);
        return (V1Status) localVarResp.getData();
    }

    public ApiResponse<V1Status> deleteProjectWithHttpInfo(String name) throws ApiException {
        Call localVarCall = this.deleteProjectValidateBeforeCall(name, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call deleteProjectAsync(String name, ApiCallback<V1Status> _callback) throws ApiException {
        Call localVarCall = this.deleteProjectValidateBeforeCall(name, _callback);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }


    // MARK: 查看Project
    public Call readProjectCall(String name, ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;
//        /projects/{project_name_or_id}
        String localVarPath = "/projects/{name}".replaceAll("\\{name\\}", this.localVarApiClient.escapeString(name.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call readProjectValidateBeforeCall(String name, ApiCallback _callback) throws ApiException {
        if (name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling readNamespace(Async)");
        } else {
            Call localVarCall = this.readProjectCall(name, _callback);
            return localVarCall;
        }
    }

    public V1Project readProject(String name) throws ApiException {
        ApiResponse<V1Project> localVarResp = this.readProjectWithHttpInfo(name);
        return (V1Project) localVarResp.getData();
    }

    public ApiResponse<V1Project> readProjectWithHttpInfo(String name) throws ApiException {
        Call localVarCall = this.readProjectValidateBeforeCall(name, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call readProjectAsync(String name, ApiCallback<V1Project> _callback) throws ApiException {
        Call localVarCall = this.readProjectValidateBeforeCall(name, _callback);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 列表Project
    public Call listProjectCall(String q,
                                Integer page,
                                Integer page_size,
                                String sort,
                                String name,
                                Boolean _public,
                                String owner,
                                Boolean with_detail,
                                ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/projects";
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        if (q != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("q", q));
        }

        if (page != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page", page));
        }

        if (page_size != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page_size", page_size));
        }

        if (sort != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("sort", sort));
        }

        if (name != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("name", name));
        }

        if (_public != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("public", _public));
        }

        if (owner != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("owner", owner));
        }

        if (with_detail != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_detail", with_detail));
        }

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();

        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"Authorization"};

        return this.localVarApiClient.buildCall(
                localVarPath,
                "GET",
                localVarQueryParams,
                localVarCollectionQueryParams,
                localVarPostBody,
                localVarHeaderParams,
                localVarCookieParams,
                localVarFormParams,
                localVarAuthNames,
                _callback);
    }

    private Call listProjectValidateBeforeCall(String q,
                                               Integer page,
                                               Integer page_size,
                                               String sort,
                                               String name,
                                               Boolean _public,
                                               String owner,
                                               Boolean with_detail,
                                               ApiCallback _callback) throws ApiException {
        Call localVarCall = this.listProjectCall(q, page, page_size, sort, name, _public, owner, with_detail, _callback);
        return localVarCall;
    }


    public ApiResponse<List<V1Project>> listProjectWithHttpInfo(String q,
                                                              Integer page,
                                                              Integer page_size,
                                                              String sort,
                                                              String name,
                                                              Boolean _public,
                                                              String owner,
                                                              Boolean with_detail) throws ApiException {

        Call localVarCall = this.listProjectValidateBeforeCall(
                q, page, page_size, sort, name,
                _public, owner, with_detail, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<List<V1Project>>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public V1ProjectList listProject(String q,
                                     Integer page,
                                     Integer page_size,
                                     String sort,
                                     String name,
                                     Boolean _public,
                                     String owner,
                                     Boolean with_detail) throws ApiException {

        ApiResponse<List<V1Project>> localVarResp = this.listProjectWithHttpInfo(q, page,
                page_size, sort, name, _public, owner, with_detail);

        Map<String, List<String>> headers = localVarResp.getHeaders();
        List<String> tcs = headers.get("x-total-count");
        String tc = tcs.get(0);
//        Link
        List<String> links = headers.get("link");

        List<V1Project> data = localVarResp.getData();
        V1ProjectList result = new V1ProjectList();
        result.setItems(data);
        result.setTotalCount(Integer.parseInt(tc));
        if(links != null && links.size() > 0) {

        }
        return result;
    }


    public Call listProjectAsync(String q,
                                 Integer page,
                                 Integer page_size,
                                 String sort,
                                 String name,
                                 Boolean _public,
                                 String owner,
                                 Boolean with_detail,
                                 ApiCallback<List<V1Project>> _callback) throws ApiException {
        Call localVarCall = this.listProjectValidateBeforeCall(q, page,
                page_size, sort, name, _public, owner, with_detail, _callback);
        Type localVarReturnType = (new TypeToken<List<V1Project>>() {

        }).getType();
        // 异步数据返回出错
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

// MARK: 更新Project

    public Call replaceProjectCall(String name,
                                   V1Project body,
                                   ApiCallback _callback) throws ApiException {
//        /projects/{project_name_or_id}
        String localVarPath = "/projects/{name}".replaceAll("\\{name\\}", this.localVarApiClient.escapeString(name.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, body, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call replaceProjectValidateBeforeCall(String name,
                                                  V1Project body,
                                                  ApiCallback _callback) throws ApiException {
        if (name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling replaceNamespace(Async)");
        } else if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceNamespace(Async)");
        } else {
            Call localVarCall = this.replaceProjectCall(name, body, _callback);
            return localVarCall;
        }
    }

    public V1Project replaceProject(String name, V1Project body) throws ApiException {
        ApiResponse<V1Project> localVarResp = this.replaceProjectWithHttpInfo(name, body);
        return (V1Project) localVarResp.getData();
    }

    public ApiResponse<V1Project> replaceProjectWithHttpInfo(String name, V1Project body) throws ApiException {
        Call localVarCall = this.replaceProjectValidateBeforeCall(name, body, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call replaceProjectAsync(String name, V1Project body, ApiCallback<V1Project> _callback) throws ApiException {
        Call localVarCall = this.replaceProjectValidateBeforeCall(name, body, _callback);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    //-------------------//

    // MARK: 删除 Repository
    public Call deleteProjectedRepositoryCall(String projectName,
                                              String repositoryName,
                                              ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;
//        /projects/{project_name}/repositories/{repository_name}
        String localVarPath = "/projects/{project_name}/repositories/{repository_name}"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(projectName.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repositoryName.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call deleteProjectedRepositoryValidateBeforeCall(String projectName,
                                                             String repositoryName,
                                                             ApiCallback _callback) throws ApiException {
        if (projectName == null) {
            throw new ApiException("Missing the required parameter 'projectName' when calling deleteNamespace(Async)");
        }
        if (repositoryName == null) {
            throw new ApiException("Missing the required parameter 'repositoryName' when calling deleteNamespace(Async)");
        } else {
            Call localVarCall = this.deleteProjectedRepositoryCall(
                    projectName,
                    repositoryName,
                    _callback);
            return localVarCall;
        }
    }

    public V1Status deleteProjectedRepository(String projectName,
                                              String repositoryName) throws ApiException {
        ApiResponse<V1Status> localVarResp = this.deleteProjectedRepositoryWithHttpInfo(projectName, repositoryName);
        return (V1Status) localVarResp.getData();
    }

    public ApiResponse<V1Status> deleteProjectedRepositoryWithHttpInfo(String projectName,
                                                                       String repositoryName) throws ApiException {
        Call localVarCall = this.deleteProjectedRepositoryValidateBeforeCall(projectName, repositoryName, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call deleteProjectedRepositoryAsync(String projectName,
                                               String repositoryName,
                                               ApiCallback<V1Status> _callback) throws ApiException {
        Call localVarCall = this.deleteProjectedRepositoryValidateBeforeCall(projectName,
                repositoryName, _callback);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }


    // MARK: 查看 Repository
    public Call readProjectedRepositoryCall(String projectName,
                                            String repositoryName,
                                            ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;
//        /projects/{project_name}/repositories/{repository_name}
        String localVarPath = "/projects/{project_name}/repositories/{repository_name}"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(projectName.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repositoryName.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call readProjectedRepositoryValidateBeforeCall(String projectName,
                                                           String repositoryName,
                                                           ApiCallback _callback) throws ApiException {
        if (projectName == null) {
            throw new ApiException("Missing the required parameter 'projectName' when calling readNamespace(Async)");
        } else if (repositoryName == null) {
            throw new ApiException("Missing the required parameter 'repositoryName' when calling readNamespace(Async)");
        } else {
            Call localVarCall = this.readProjectedRepositoryCall(projectName, repositoryName, _callback);
            return localVarCall;
        }
    }

    public V1Repository readProjectedRepository(String projectName,
                                                String repositoryName) throws ApiException {
        ApiResponse<V1Repository> localVarResp = this.readProjectedRepositoryWithHttpInfo(projectName, repositoryName);
        return (V1Repository) localVarResp.getData();
    }

    public ApiResponse<V1Repository> readProjectedRepositoryWithHttpInfo(
            String projectName,
            String repositoryName) throws ApiException {
        Call localVarCall = this.readProjectedRepositoryValidateBeforeCall(projectName,
                repositoryName, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Repository>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call readProjectedRepositoryAsync(String projectName,
                                             String repositoryName,
                                             ApiCallback<V1Repository> _callback) throws ApiException {
        Call localVarCall = this.readProjectedRepositoryValidateBeforeCall(
                projectName,
                repositoryName, _callback);
        Type localVarReturnType = (new TypeToken<V1Repository>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 列表  Projected 下得 Repository
    public Call listProjectedRepositoryCall(
            String projectName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;
//        //projects/{project_name}/repositories
        String localVarPath = "/projects/{project_name}/repositories"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(projectName.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();
        if (q != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("q", q));
        }

        if (sort != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("sort", sort));
        }

        if (page != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page", page));
        }

        if (page_size != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page_size", page_size));
        }


        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(
                localVarPath,
                "GET",
                localVarQueryParams,
                localVarCollectionQueryParams,
                localVarPostBody,
                localVarHeaderParams,
                localVarCookieParams,
                localVarFormParams,
                localVarAuthNames,
                _callback);
    }

    private Call listProjectedRepositoryValidateBeforeCall(
            String projectName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            ApiCallback _callback) throws ApiException {

        if (projectName == null) {
            throw new ApiException("Missing the required parameter 'projectName' when calling listProjectedRepository(Async)");
        }

        Call localVarCall = this.listProjectedRepositoryCall(
                projectName, q, sort, page, page_size, _callback);

        return localVarCall;
    }

    public ApiResponse<V1ProjectList> listProjectedRepositoryWithHttpInfo(
            String projectName,
            String q, String sort, Integer page, Integer page_size) throws ApiException {

        Call localVarCall = this.listProjectedRepositoryValidateBeforeCall(
                projectName, q, sort, page, page_size, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1ProjectList>() {
        }).getType();

        return this.localVarApiClient.execute(localVarCall, localVarReturnType);

    }

    public V1ProjectList listProjectedRepository(
            String projectName,
            String q, String sort, Integer page, Integer page_size) throws ApiException {

        ApiResponse<V1ProjectList> localVarResp = this.listProjectedRepositoryWithHttpInfo(
                projectName,
                q, sort, page, page_size);

        return (V1ProjectList) localVarResp.getData();
    }

    public Call listProjectedRepositoryAsync(
            String projectName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            ApiCallback<V1ProjectList> _callback) throws ApiException {

        Call localVarCall = this.listProjectedRepositoryValidateBeforeCall(
                projectName, q, sort, page, page_size, _callback);
        Type localVarReturnType = (new TypeToken<V1ProjectList>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 列表 所有 Projected  Repository
    public Call listRepositoryForAllProjectCall(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;
//        /repositories
//        //repositories
        String localVarPath = "/repositories";

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();
        if (q != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("q", q));
        }

        if (sort != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("sort", sort));
        }

        if (page != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page", page));
        }

        if (page_size != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page_size", page_size));
        }


        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(
                localVarPath,
                "GET",
                localVarQueryParams,
                localVarCollectionQueryParams,
                localVarPostBody,
                localVarHeaderParams,
                localVarCookieParams,
                localVarFormParams,
                localVarAuthNames,
                _callback);
    }

    private Call listRepositoryForAllProjectValidateBeforeCall(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            ApiCallback _callback) throws ApiException {


        Call localVarCall = this.listRepositoryForAllProjectCall(
                q, sort, page, page_size, _callback);

        return localVarCall;
    }

    public ApiResponse<V1ProjectList> listRepositoryForAllProjectWithHttpInfo(
            String q, String sort, Integer page, Integer page_size) throws ApiException {

        Call localVarCall = this.listRepositoryForAllProjectValidateBeforeCall(
                q, sort, page, page_size, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1ProjectList>() {
        }).getType();

        return this.localVarApiClient.execute(localVarCall, localVarReturnType);

    }

    public V1ProjectList listRepositoryForAllProject(
            String q, String sort, Integer page, Integer page_size) throws ApiException {

        ApiResponse<V1ProjectList> localVarResp = this.listRepositoryForAllProjectWithHttpInfo(
                q, sort, page, page_size);

        return (V1ProjectList) localVarResp.getData();
    }

    public Call listRepositoryForAllProjectAsync(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            ApiCallback<V1ProjectList> _callback) throws ApiException {

        Call localVarCall = this.listRepositoryForAllProjectValidateBeforeCall(q, sort, page, page_size, _callback);
        Type localVarReturnType = (new TypeToken<V1ProjectList>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 更新 Repository
    public Call replaceProjectedRepositoryCall(
            String projectName,
            String repositoryName,
            V1Repository body,
            ApiCallback _callback) throws ApiException {
//        /projects/{project_name_or_id}
        String localVarPath = "/projects/{project_name}/repositories/{repository_name}"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(projectName.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repositoryName.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, body, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call replaceProjectedRepositoryValidateBeforeCall(
            String projectName,
            String repositoryName,
            V1Repository body,
            ApiCallback _callback) throws ApiException {
        if (projectName == null) {
            throw new ApiException("Missing the required parameter 'projectName' when calling replaceProjectedRepository(Async)");
        } else if (repositoryName == null) {
            throw new ApiException("Missing the required parameter 'repositoryName' when calling replaceProjectedRepository(Async)");
        } else if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceNamespace(Async)");
        } else {
            Call localVarCall = this.replaceProjectedRepositoryCall(projectName, repositoryName, body, _callback);
            return localVarCall;
        }
    }

    public V1Repository replaceProjectedRepository(
            String projectName,
            String repositoryName,
            V1Repository body) throws ApiException {
        ApiResponse<V1Repository> localVarResp = this.replaceProjectedRepositoryWithHttpInfo(projectName, repositoryName, body);
        return (V1Repository) localVarResp.getData();
    }

    public ApiResponse<V1Repository> replaceProjectedRepositoryWithHttpInfo(String projectName,
                                                                            String repositoryName, V1Repository body) throws ApiException {
        Call localVarCall = this.replaceProjectedRepositoryValidateBeforeCall(projectName, repositoryName, body, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Repository>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call replaceProjectedRepositoryAsync(String projectName,
                                                String repositoryName,
                                                V1Repository body,
                                                ApiCallback<V1Repository> _callback) throws ApiException {
        Call localVarCall = this.replaceProjectedRepositoryValidateBeforeCall(projectName, repositoryName, body, _callback);
        Type localVarReturnType = (new TypeToken<V1Repository>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 列表 artifacts
    public Call listProjectedRepositoryArtifactsCall(
            String projectName,
            String repositoryName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_signature,
            Boolean with_immutable_status,
            Boolean with_accessory,
            ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;

        String localVarPath = "projects/{project_name}/repositories/{repository_name}/artifacts"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(projectName.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repositoryName.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        if (q != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("q", q));
        }

        if (page != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page", page));
        }

        if (page_size != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page_size", page_size));
        }

        if (sort != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("sort", sort));
        }

        if (with_tag != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_tag", with_tag));
        }

        if (with_label != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_label", with_label));
        }

        if (with_scan_overview != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_scan_overview", with_scan_overview));
        }

        if (with_signature != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_signature", with_signature));
        }

        if (with_immutable_status != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_immutable_status", with_immutable_status));
        }

        if (with_accessory != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_accessory", with_accessory));
        }

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(
                localVarPath,
                "GET",
                localVarQueryParams,
                localVarCollectionQueryParams,
                localVarPostBody,
                localVarHeaderParams,
                localVarCookieParams,
                localVarFormParams,
                localVarAuthNames,
                _callback);
    }

    private Call listProjectedRepositoryArtifactsValidateBeforeCall(
            String projectName,
            String repositoryName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_signature,
            Boolean with_immutable_status,
            Boolean with_accessory,
            ApiCallback _callback) throws ApiException {

        if (projectName == null) {

        }

        if (repositoryName == null) {

        }

        Call localVarCall = this.listProjectedRepositoryArtifactsCall(projectName,
                repositoryName,
                q,
                sort,
                page,
                page_size,
                with_tag,
                with_label,
                with_scan_overview,
                with_signature,
                with_immutable_status,
                with_accessory, _callback);
        return localVarCall;
    }


    public ApiResponse<V1ListArtifact> listProjectedRepositoryArtifactsWithHttpInfo(
            String projectName,
            String repositoryName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_signature,
            Boolean with_immutable_status,
            Boolean with_accessory
    ) throws ApiException {
        Call localVarCall = this.listProjectedRepositoryArtifactsValidateBeforeCall(
                projectName,
                repositoryName,
                q,
                sort,
                page,
                page_size,
                with_tag,
                with_label,
                with_scan_overview,
                with_signature,
                with_immutable_status,
                with_accessory
                , (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1ListArtifact>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public V1ListArtifact listProjectedRepositoryArtifacts(
            String projectName,
            String repositoryName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_signature,
            Boolean with_immutable_status,
            Boolean with_accessory
    ) throws ApiException {
        ApiResponse<V1ListArtifact> localVarResp = this.listProjectedRepositoryArtifactsWithHttpInfo(
                projectName,
                repositoryName,
                q,
                sort,
                page,
                page_size,
                with_tag,
                with_label,
                with_scan_overview,
                with_signature,
                with_immutable_status,
                with_accessory);
        return (V1ListArtifact) localVarResp.getData();
    }

    public Call listProjectedRepositoryArtifactsAsync(
            String projectName,
            String repositoryName,
            String q,
            String sort,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_signature,
            Boolean with_immutable_status,
            Boolean with_accessory,

            ApiCallback<V1ListArtifact> _callback) throws ApiException {
        Call localVarCall = this.listProjectedRepositoryArtifactsValidateBeforeCall(
                projectName,
                repositoryName,
                q,
                sort,
                page,
                page_size,
                with_tag,
                with_label,
                with_scan_overview,
                with_signature,
                with_immutable_status,
                with_accessory,
                _callback);
        Type localVarReturnType = (new TypeToken<V1ListArtifact>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }


    // MARK: 查看 artifact
    public Call readProjectedRepositoryArtifactCall(
            String project_name,
            String repository_name,
            String reference,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_accessory,
            Boolean with_signature,
            Boolean with_immutable_status,
            ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;

        String localVarPath = "/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(project_name.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repository_name.toString()))
                .replaceAll("\\{reference\\}", this.localVarApiClient.escapeString(reference.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        if (page != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page", page));
        }

        if (page_size != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page_size", page_size));
        }

        if (with_tag != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_tag", with_tag));
        }

        if (with_label != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_label", with_label));
        }

        if (with_scan_overview != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_scan_overview", with_scan_overview));
        }

        if (with_signature != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_signature", with_signature));
        }

        if (with_immutable_status != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_immutable_status", with_immutable_status));
        }

        if (with_accessory != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("with_accessory", with_accessory));
        }


        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);


        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call readProjectedRepositoryArtifactValidateBeforeCall(
            String project_name,
            String repository_name,
            String reference,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_accessory,
            Boolean with_signature,
            Boolean with_immutable_status,
            ApiCallback _callback) throws ApiException {

        if (project_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling readNamespace(Async)");
        }
        if (repository_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling readNamespace(Async)");
        }
        if (reference == null) {
            throw new ApiException("Missing the required parameter 'name' when calling readNamespace(Async)");
        } else {
            Call localVarCall = this.readProjectedRepositoryArtifactCall(
                    project_name,
                    repository_name,
                    reference,
                    page,
                    page_size,
                    with_tag,
                    with_label,
                    with_scan_overview,
                    with_accessory,
                    with_signature,
                    with_immutable_status,
                    _callback);
            return localVarCall;
        }
    }

    public V1Project readProjectedRepositoryArtifact(
            String project_name,
            String repository_name,
            String reference,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_accessory,
            Boolean with_signature,
            Boolean with_immutable_status
    ) throws ApiException {
        ApiResponse<V1Project> localVarResp = this.readProjectedRepositoryArtifactWithHttpInfo(
                project_name,
                repository_name,
                reference,
                page,
                page_size,
                with_tag,
                with_label,
                with_scan_overview,
                with_accessory,
                with_signature,
                with_immutable_status
        );
        return (V1Project) localVarResp.getData();
    }

    public ApiResponse<V1Project> readProjectedRepositoryArtifactWithHttpInfo(
            String project_name,
            String repository_name,
            String reference,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_accessory,
            Boolean with_signature,
            Boolean with_immutable_status
    ) throws ApiException {
        Call localVarCall = this.readProjectedRepositoryArtifactValidateBeforeCall(
                project_name,
                repository_name,
                reference,
                page,
                page_size,
                with_tag,
                with_label,
                with_scan_overview,
                with_accessory,
                with_signature,
                with_immutable_status,
                (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call readProjectedRepositoryArtifactAsync(
            String project_name,
            String repository_name,
            String reference,
            Integer page,
            Integer page_size,
            Boolean with_tag,
            Boolean with_label,
            Boolean with_scan_overview,
            Boolean with_accessory,
            Boolean with_signature,
            Boolean with_immutable_status,
            ApiCallback<V1Project> _callback) throws ApiException {
        Call localVarCall = this.readProjectedRepositoryArtifactValidateBeforeCall(
                project_name,
                repository_name,
                reference,
                page,
                page_size,
                with_tag,
                with_label,
                with_scan_overview,
                with_accessory,
                with_signature,
                with_immutable_status,
                _callback);
        Type localVarReturnType = (new TypeToken<V1Project>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 删除 artifact
    public Call deleteProjectedRepositoryArtifactCall(
            String project_name,
            String repository_name,
            String reference,
            ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;
        String localVarPath = "/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(project_name.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repository_name.toString()))
                .replaceAll("\\{reference\\}", this.localVarApiClient.escapeString(reference.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call deleteProjectedRepositoryArtifactValidateBeforeCall(
            String project_name,
            String repository_name,
            String reference,
            ApiCallback _callback) throws ApiException {
        if (project_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (repository_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (reference == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else {
            Call localVarCall = this.deleteProjectedRepositoryArtifactCall(
                    project_name, repository_name, reference, _callback);
            return localVarCall;
        }
    }

    public V1Status deleteProjectedRepositoryArtifact(String project_name,
                                                      String repository_name,
                                                      String reference) throws ApiException {
        ApiResponse<V1Status> localVarResp = this.deleteProjectedRepositoryArtifactWithHttpInfo(
                project_name, repository_name, reference);
        return (V1Status) localVarResp.getData();
    }

    public ApiResponse<V1Status> deleteProjectedRepositoryArtifactWithHttpInfo(String project_name,
                                                                               String repository_name,
                                                                               String reference) throws ApiException {
        Call localVarCall = this.deleteProjectedRepositoryArtifactValidateBeforeCall(project_name, repository_name, reference, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call deleteProjectedRepositoryArtifactAsync(String project_name,
                                                       String repository_name,
                                                       String reference,
                                                       ApiCallback<V1Status> _callback) throws ApiException {

        Call localVarCall = this.deleteProjectedRepositoryArtifactValidateBeforeCall(project_name, repository_name, reference, _callback);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }


    // MARK: 添加标签 artifact
    public Call addProjectedRepositoryArtifactLabelCall(
            String project_name,
            String repository_name,
            String reference,
            V1Label body,
            ApiCallback _callback) throws ApiException {

        Object localVarPostBody = body;
        String localVarPath = "/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/labels"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(project_name.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repository_name.toString()))
                .replaceAll("\\{reference\\}", this.localVarApiClient.escapeString(reference.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call addProjectedRepositoryArtifactLabelValidateBeforeCall(
            String project_name,
            String repository_name,
            String reference,
            V1Label body,
            ApiCallback _callback) throws ApiException {
        if (project_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (repository_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (reference == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (body == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else {
            Call localVarCall = this.addProjectedRepositoryArtifactLabelCall(
                    project_name, repository_name, reference, body, _callback);
            return localVarCall;
        }
    }

    public V1Status addProjectedRepositoryArtifactLabel(String project_name,
                                                        String repository_name,
                                                        String reference,
                                                        V1Label body) throws ApiException {
        ApiResponse<V1Status> localVarResp = this.addProjectedRepositoryArtifactLabelWithHttpInfo(
                project_name, repository_name, reference, body);
        return (V1Status) localVarResp.getData();
    }

    public ApiResponse<V1Status> addProjectedRepositoryArtifactLabelWithHttpInfo(String project_name,
                                                                                 String repository_name,
                                                                                 String reference,
                                                                                 V1Label body) throws ApiException {
        Call localVarCall = this.addProjectedRepositoryArtifactLabelValidateBeforeCall(project_name, repository_name, reference, body, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call addProjectedRepositoryArtifactLabelAsync(String project_name,
                                                         String repository_name,
                                                         String reference,
                                                         V1Label body,
                                                         ApiCallback<V1Status> _callback) throws ApiException {

        Call localVarCall = this.addProjectedRepositoryArtifactLabelValidateBeforeCall(project_name, repository_name, reference, body, _callback);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 删除标签 artifact
    public Call deleteProjectedRepositoryArtifactLabelCall(
            String project_name,
            String repository_name,
            String reference,
            String label_id,
            ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;
        String localVarPath = "/projects/{project_name}/repositories/{repository_name}/artifacts/{reference}/labels/{label_id}"
                .replaceAll("\\{project_name\\}", this.localVarApiClient.escapeString(project_name.toString()))
                .replaceAll("\\{repository_name\\}", this.localVarApiClient.escapeString(repository_name.toString()))
                .replaceAll("\\{reference\\}", this.localVarApiClient.escapeString(reference.toString()))
                .replaceAll("\\{label_id\\}", this.localVarApiClient.escapeString(label_id.toString()));

        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call deleteProjectedRepositoryArtifactLabelValidateBeforeCall(
            String project_name,
            String repository_name,
            String reference,
            String label_id,
            ApiCallback _callback) throws ApiException {
        if (project_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (repository_name == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (reference == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else if (label_id == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else {
            Call localVarCall = this.deleteProjectedRepositoryArtifactLabelCall(
                    project_name, repository_name, reference, label_id, _callback);
            return localVarCall;
        }
    }

    public V1Status deleteProjectedRepositoryArtifactLabel(String project_name,
                                                           String repository_name,
                                                           String reference,
                                                           String label_id) throws ApiException {
        ApiResponse<V1Status> localVarResp = this.deleteProjectedRepositoryArtifactLabelWithHttpInfo(
                project_name, repository_name, reference, label_id);
        return (V1Status) localVarResp.getData();
    }

    public ApiResponse<V1Status> deleteProjectedRepositoryArtifactLabelWithHttpInfo(String project_name,
                                                                                    String repository_name,
                                                                                    String reference,
                                                                                    String label_id) throws ApiException {
        Call localVarCall = this.deleteProjectedRepositoryArtifactLabelValidateBeforeCall(
                project_name, repository_name, reference, label_id, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call deleteProjectedRepositoryArtifactLabelAsync(String project_name,
                                                            String repository_name,
                                                            String reference,
                                                            String label_id,
                                                            ApiCallback<V1Status> _callback) throws ApiException {

        Call localVarCall = this.deleteProjectedRepositoryArtifactLabelValidateBeforeCall(project_name, repository_name, reference, label_id, _callback);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 创建 label
    public Call createLabelCall(V1Label body, ApiCallback _callback) throws ApiException {

        String localVarPath = "/labels";
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, body, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call createLabelValidateBeforeCall(V1Label body, ApiCallback _callback) throws ApiException {
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createNamespace(Async)");
        } else {
            Call localVarCall = this.createLabelCall(body, _callback);
            return localVarCall;
        }
    }

    public V1Label createLabel(V1Label body) throws ApiException {
        ApiResponse<V1Label> localVarResp = this.createLabelWithHttpInfo(body);
        return (V1Label) localVarResp.getData();
    }

    public ApiResponse<V1Label> createLabelWithHttpInfo(V1Label body) throws ApiException {
        Call localVarCall = this.createLabelValidateBeforeCall(body, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Label>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call createLabelAsync(V1Label body, ApiCallback<V1Label> _callback) throws ApiException {
        Call localVarCall = this.createLabelValidateBeforeCall(body, _callback);
        Type localVarReturnType = (new TypeToken<V1Label>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 删除 label
    public Call deleteLabelCall(Integer label_id, ApiCallback _callback) throws ApiException {

        Object localVarPostBody = null;
        String localVarPath = "/labels/{label_id}".replaceAll("\\{label_id\\}", this.localVarApiClient.escapeString(label_id.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call deleteLabelValidateBeforeCall(Integer label_id, ApiCallback _callback) throws ApiException {
        if (label_id == null) {
            throw new ApiException("Missing the required parameter 'name' when calling deleteNamespace(Async)");
        } else {
            Call localVarCall = this.deleteLabelCall(label_id, _callback);
            return localVarCall;
        }
    }

    public V1Status deleteLabel(Integer label_id) throws ApiException {
        ApiResponse<V1Status> localVarResp = this.deleteLabelWithHttpInfo(label_id);
        return (V1Status) localVarResp.getData();
    }

    public ApiResponse<V1Status> deleteLabelWithHttpInfo(Integer label_id) throws ApiException {
        Call localVarCall = this.deleteLabelValidateBeforeCall(label_id, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call deleteLabelAsync(Integer label_id, ApiCallback<V1Status> _callback) throws ApiException {
        Call localVarCall = this.deleteLabelValidateBeforeCall(label_id, _callback);
        Type localVarReturnType = (new TypeToken<V1Status>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }


    // MARK: 查看 label
    public Call readLabelCall(Integer label_id, ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/labels/{label_id}".replaceAll("\\{label_id\\}", this.localVarApiClient.escapeString(label_id.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call readLabelValidateBeforeCall(Integer label_id, ApiCallback _callback) throws ApiException {
        if (label_id == null) {
            throw new ApiException("Missing the required parameter 'name' when calling readNamespace(Async)");
        } else {
            Call localVarCall = this.readLabelCall(label_id, _callback);
            return localVarCall;
        }
    }

    public V1Label readLabel(Integer label_id) throws ApiException {
        ApiResponse<V1Label> localVarResp = this.readLabelWithHttpInfo(label_id);
        return (V1Label) localVarResp.getData();
    }

    public ApiResponse<V1Label> readLabelWithHttpInfo(Integer label_id) throws ApiException {
        Call localVarCall = this.readLabelValidateBeforeCall(label_id, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Label>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call readLabelAsync(Integer label_id, ApiCallback<V1Label> _callback) throws ApiException {
        Call localVarCall = this.readLabelValidateBeforeCall(label_id, _callback);
        Type localVarReturnType = (new TypeToken<V1Label>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 列表 label
    public Call listLabelCall(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            String name,
            String scope,
            Integer project_id,
            ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/labels";
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();
        if (q != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("q", q));
        }

        if (page != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page", page));
        }

        if (page_size != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("page_size", page_size));
        }

        if (sort != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("sort", sort));
        }

        if (name != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("name", name));
        }

        if (scope != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("scope", scope));
        }

        if (project_id != null) {
            localVarQueryParams.addAll(this.localVarApiClient.parameterToPair("project_id", project_id));
        }


        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(
                localVarPath,
                "GET",
                localVarQueryParams,
                localVarCollectionQueryParams,
                localVarPostBody,
                localVarHeaderParams,
                localVarCookieParams,
                localVarFormParams,
                localVarAuthNames,
                _callback);
    }

    private Call listLabelValidateBeforeCall(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            String name,
            String scope,
            Integer project_id,
            ApiCallback _callback) throws ApiException {
        Call localVarCall = this.listLabelCall(
                q,
                sort,
                page,
                page_size,
                name,
                scope,
                project_id
                , _callback);
        return localVarCall;
    }


    public ApiResponse<V1LabelList> listLabelWithHttpInfo(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            String name,
            String scope,
            Integer project_id
    ) throws ApiException {
        Call localVarCall = this.listLabelValidateBeforeCall(
                q,
                sort,
                page,
                page_size,
                name,
                scope,
                project_id,
                (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1LabelList>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public V1LabelList listLabel(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            String name,
            String scope,
            Integer project_id
    ) throws ApiException {
        ApiResponse<V1LabelList> localVarResp = this.listLabelWithHttpInfo(
                q,
                sort,
                page,
                page_size,
                name,
                scope,
                project_id);
        return (V1LabelList) localVarResp.getData();
    }


    public Call listLabelAsync(
            String q,
            String sort,
            Integer page,
            Integer page_size,
            String name,
            String scope,
            Integer project_id,
            ApiCallback<V1LabelList> _callback) throws ApiException {
        Call localVarCall = this.listLabelValidateBeforeCall(q,
                sort,
                page,
                page_size,
                name,
                scope,
                project_id, _callback);
        Type localVarReturnType = (new TypeToken<V1LabelList>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    // MARK: 更新 label

    public Call replaceLabelCall(Integer label_id,
                                 V1Label body,
                                 ApiCallback _callback) throws ApiException {
        String localVarPath = "/labels/{label_id}".replaceAll("\\{label_id\\}", this.localVarApiClient.escapeString(label_id.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        List<Pair> localVarCollectionQueryParams = new ArrayList();

        Map<String, String> localVarHeaderParams = new HashMap();
        Map<String, String> localVarCookieParams = new HashMap();
        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "application/yaml", "application/vnd.kubernetes.protobuf"};
        String localVarAccept = this.localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[0];
        String localVarContentType = this.localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"BasicAuth"};
        return this.localVarApiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, body, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    private Call replaceLabelValidateBeforeCall(Integer label_id,
                                                V1Label body,
                                                ApiCallback _callback) throws ApiException {
        if (label_id == null) {
            throw new ApiException("Missing the required parameter 'name' when calling replaceNamespace(Async)");
        } else if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceNamespace(Async)");
        } else {
            Call localVarCall = this.replaceLabelCall(label_id, body, _callback);
            return localVarCall;
        }
    }

    public V1Label replaceLabel(Integer label_id, V1Label body) throws ApiException {
        ApiResponse<V1Label> localVarResp = this.replaceLabelWithHttpInfo(label_id, body);
        return (V1Label) localVarResp.getData();
    }

    public ApiResponse<V1Label> replaceLabelWithHttpInfo(Integer label_id, V1Label body) throws ApiException {
        Call localVarCall = this.replaceLabelValidateBeforeCall(label_id, body, (ApiCallback) null);
        Type localVarReturnType = (new TypeToken<V1Label>() {
        }).getType();
        return this.localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    public Call replaceLabelAsync(Integer label_id, V1Label body, ApiCallback<V1Label> _callback) throws ApiException {
        Call localVarCall = this.replaceLabelValidateBeforeCall(label_id, body, _callback);
        Type localVarReturnType = (new TypeToken<V1Label>() {
        }).getType();
        this.localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}
