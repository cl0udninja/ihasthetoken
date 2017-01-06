import { Injectable } from '@angular/core';
import { Http, RequestOptions, Headers, ResponseContentType, Request, URLSearchParams, RequestMethod }
  from '@angular/http';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class Ajax {
  constructor(private http: Http) { }

  /**
   * ajax: makes a request with the appropriate proxy.
   *
   * @param {String} method for the request
   * @param {String} the url to call
   * @param {String} params querystring params for the request
   * @param {String} data POST data for the request
   * @return {Object} promise got the call
   */
  private ajax(
    method: string | RequestMethod,
    url: string,
    params = new URLSearchParams(),
    data = {},
    headers = new Headers()): Promise<any> {

    headers.append('Accept', 'application/json, */*; q=0.01');
    headers.append('X-Requested-With', 'XMLHttpRequest');
    headers.append('Content-Type', 'application/json');

    let requestOptions = new RequestOptions({
      url: url,
      body: data,
      method: method,
      headers: headers,
      responseType: ResponseContentType.Json,
      search: params,
      withCredentials: true
    });

    return this.http.request(new Request(requestOptions))
      .toPromise()
      .then(result => result.json())
      .catch(error => this.handleAjaxError(error));
  }

  /**
   * handleAjaxErrors
   *
   * @param  {type} errors description
   * @return {type}        description
   */
  private handleAjaxError(error) {
    console.error(error);
    throw error; // propagate the initial error
  }

  // tslint:disable-next-line: no-reserved-keywords
  public get(
    url: string,
    params = new URLSearchParams(),
    data = {},
    headers = new Headers()): Promise<any> {
    return this.ajax(RequestMethod.Get, url, params, data, headers);
  }

  public put(
    url: string,
    params = new URLSearchParams(),
    data = {},
    headers = new Headers()): Promise<any> {
    return this.ajax(RequestMethod.Put, url, params, data, headers);
  }

  public post(
    url: string,
    params = new URLSearchParams(),
    data = {},
    headers = new Headers()): Promise<any> {
    return this.ajax(RequestMethod.Post, url, params, data, headers);
  }

  // tslint:disable-next-line: no-reserved-keywords
  public delete(
    url: string,
    params = new URLSearchParams(),
    data = {},
    headers = new Headers()): Promise<any> {
    return this.ajax(RequestMethod.Delete, url, params, data, headers);
  }

  public patch(
    url: string,
    params = new URLSearchParams(),
    data = {},
    headers = new Headers()): Promise<any> {
    return this.ajax(RequestMethod.Patch, url, params, data, headers);
  }

  /**
   * Polls a query task url until it's finished, failed or cancelled
   *
   * @param  {String} pollUrl      The URL to poll
   * @param  {Number} interval The interval in between polls, defaults to 500ms
   *
   * @return {Promise}          The promise that is resolved with the poll results
   * if the poll finishes or rejected otherwise with an error or the failed queryResult
   */
  public pollQueryUrl(pollUrl: string, interval = 500): Promise<any> {
    return new Promise((resolve, reject) => {
      let pollId;
      let pollQuery = (url) => {
        this.get(url).then(queryResult => {
          if (!queryResult || !queryResult.taskInfo) {
            console.warn('Unknown query poll response', queryResult);
            clearInterval(pollId);
            reject(queryResult);
            return;
          }
          switch (queryResult.taskInfo.stage) {
            case 'FAILED':
            case 'CANCELLED':
              clearInterval(pollId);
              reject(queryResult);
              break;
            case 'FINISHED':
              clearInterval(pollId);
              resolve(queryResult);
              break;
            default:
              console.warn('Unknown query task stage, aborting', queryResult.taskInfo.stage);
              clearInterval(pollId);
              reject(queryResult);
              break;
          }
        }).catch(error => {
          console.error('Unknown error while polling query url, aborting', error);
          clearInterval(pollId);
          reject(error);
        });
      };
      pollId = setInterval(
        () => {
          pollQuery(pollUrl);
        },
        interval);
    });
  }
}